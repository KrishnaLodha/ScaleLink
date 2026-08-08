package com.scalink.cache;

import com.scalink.dto.cache.UrlCacheEntry;
import com.scalink.entity.Url;
import com.scalink.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

/**
 * Caches globally popular URLs for hot redirect paths.
 */
@Service
@Profile("!test")
@RequiredArgsConstructor
public class PopularUrlCacheService {

    public static final String POPULAR_URLS_KEY = "cache:popular:urls";

    private final UrlRepository urlRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UrlCacheService urlCacheService;

    @Scheduled(fixedDelayString = "${scalink.cache.popular-refresh-ms:300000}")
    public void refreshPopularUrls() {
        List<Url> popular = urlRepository.findTopPopular(PageRequest.of(0, 100));
        redisTemplate.opsForValue().set(POPULAR_URLS_KEY, popular.size(), Duration.ofMinutes(10));

        for (Url url : popular) {
            UrlCacheEntry entry = UrlCacheEntry.builder()
                    .urlId(url.getId())
                    .originalUrl(url.getOriginalUrl())
                    .shortCode(url.getShortCode())
                    .customAlias(url.getCustomAlias())
                    .expirationDate(url.getExpirationDate())
                    .build();
            urlCacheService.put(url.getShortCode(), entry);
            if (url.getCustomAlias() != null) {
                urlCacheService.put(url.getCustomAlias(), entry);
            }
        }
    }
}
