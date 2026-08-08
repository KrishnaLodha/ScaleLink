package com.scalink.cache;

import com.scalink.config.CacheProperties;
import com.scalink.dto.cache.UrlCacheEntry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlCacheServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    private UrlCacheService urlCacheService;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        CacheProperties cacheProperties = new CacheProperties();
        cacheProperties.setUrlTtlSeconds(86400);
        urlCacheService = new UrlCacheService(redisTemplate, new SimpleMeterRegistry(), cacheProperties);
    }

    @Test
    void get_shouldReturnEntryOnHit() {
        UrlCacheEntry entry = UrlCacheEntry.builder()
                .urlId(1L)
                .originalUrl("https://example.com")
                .shortCode("abc1234")
                .build();

        when(valueOperations.get(CacheKeys.urlRedirect("abc1234"))).thenReturn(entry);

        Optional<UrlCacheEntry> result = urlCacheService.get("abc1234");

        assertThat(result).isPresent();
        assertThat(result.get().getOriginalUrl()).isEqualTo("https://example.com");
        assertThat(urlCacheService.getHitRatio()).isGreaterThan(0);
    }

    @Test
    void get_shouldRecordMissWhenAbsent() {
        when(valueOperations.get(CacheKeys.urlRedirect("missing"))).thenReturn(null);

        Optional<UrlCacheEntry> result = urlCacheService.get("missing");

        assertThat(result).isEmpty();
    }

    @Test
    void put_shouldStoreWithTtl() {
        UrlCacheEntry entry = UrlCacheEntry.builder()
                .urlId(1L)
                .originalUrl("https://example.com")
                .shortCode("abc1234")
                .customAlias("my-link")
                .build();

        urlCacheService.put("abc1234", entry);

        ArgumentCaptor<Duration> ttlCaptor = ArgumentCaptor.forClass(Duration.class);
        verify(valueOperations).set(eq(CacheKeys.urlRedirect("abc1234")), eq(entry), ttlCaptor.capture());
        assertThat(ttlCaptor.getValue().toSeconds()).isEqualTo(86400);
    }

    @Test
    void put_shouldSkipExpiredEntries() {
        UrlCacheEntry entry = UrlCacheEntry.builder()
                .urlId(1L)
                .originalUrl("https://example.com")
                .shortCode("abc1234")
                .expirationDate(Instant.now().minusSeconds(60))
                .build();

        urlCacheService.put("abc1234", entry);

        verify(valueOperations, org.mockito.Mockito.never())
                .set(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any(Duration.class));
    }
}
