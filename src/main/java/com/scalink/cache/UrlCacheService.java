package com.scalink.cache;

import com.scalink.dto.cache.UrlCacheEntry;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Service
@Profile("!test")
public class UrlCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Counter cacheHits;
    private final Counter cacheMisses;
    private final Timer cacheLookupTimer;
    private final long urlTtlSeconds;

    public UrlCacheService(
            RedisTemplate<String, Object> redisTemplate,
            MeterRegistry meterRegistry,
            @Autowired(required = false) com.scalink.config.CacheProperties cacheProperties) {
        this.redisTemplate = redisTemplate;
        this.cacheHits = meterRegistry.counter("scalink.cache.hits", "cache", "url");
        this.cacheMisses = meterRegistry.counter("scalink.cache.misses", "cache", "url");
        this.cacheLookupTimer = meterRegistry.timer("scalink.cache.lookup", "cache", "url");
        this.urlTtlSeconds = cacheProperties != null ? cacheProperties.getUrlTtlSeconds() : 86400L;
    }

    public Optional<UrlCacheEntry> get(String code) {
        return cacheLookupTimer.record(() -> {
            Object value = redisTemplate.opsForValue().get(CacheKeys.urlRedirect(code));
            if (value instanceof UrlCacheEntry entry) {
                cacheHits.increment();
                return Optional.of(entry);
            }
            cacheMisses.increment();
            return Optional.empty();
        });
    }

    public void put(String code, UrlCacheEntry entry) {
        if (entry.isExpired()) {
            return;
        }
        redisTemplate.opsForValue().set(
                CacheKeys.urlRedirect(code),
                entry,
                Duration.ofSeconds(urlTtlSeconds));
        if (entry.getCustomAlias() != null && !entry.getCustomAlias().equals(code)) {
            redisTemplate.opsForValue().set(
                    CacheKeys.urlRedirect(entry.getCustomAlias()),
                    entry,
                    Duration.ofSeconds(urlTtlSeconds));
        }
        if (entry.getShortCode() != null && !entry.getShortCode().equals(code)) {
            redisTemplate.opsForValue().set(
                    CacheKeys.urlRedirect(entry.getShortCode()),
                    entry,
                    Duration.ofSeconds(urlTtlSeconds));
        }
    }

    public void evict(UrlCacheEntry entry) {
        if (entry == null) {
            return;
        }
        evictKey(entry.getShortCode());
        evictKey(entry.getCustomAlias());
    }

    public void evictKey(String code) {
        if (code != null) {
            redisTemplate.delete(CacheKeys.urlRedirect(code));
        }
    }

    public double getHitRatio() {
        double hits = cacheHits.count();
        double misses = cacheMisses.count();
        double total = hits + misses;
        return total == 0 ? 0.0 : hits / total;
    }
}
