package com.scalink.cache;

import com.scalink.dto.response.AnalyticsSummaryResponse;
import com.scalink.dto.response.DashboardSummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Service
@Profile("!test")
@RequiredArgsConstructor
public class AnalyticsCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final com.scalink.config.CacheProperties cacheProperties;

    public Optional<AnalyticsSummaryResponse> getSummary(Long urlId) {
        Object value = redisTemplate.opsForValue().get(CacheKeys.analyticsSummary(urlId));
        if (value instanceof AnalyticsSummaryResponse summary) {
            return Optional.of(summary);
        }
        return Optional.empty();
    }

    public void putSummary(Long urlId, AnalyticsSummaryResponse summary) {
        redisTemplate.opsForValue().set(
                CacheKeys.analyticsSummary(urlId),
                summary,
                Duration.ofSeconds(cacheProperties.getAnalyticsTtlSeconds()));
    }

    public void evictSummary(Long urlId) {
        redisTemplate.delete(CacheKeys.analyticsSummary(urlId));
    }

    public Optional<DashboardSummaryResponse> getDashboard(Long userId) {
        Object value = redisTemplate.opsForValue().get(CacheKeys.dashboard(userId));
        if (value instanceof DashboardSummaryResponse dashboard) {
            return Optional.of(dashboard);
        }
        return Optional.empty();
    }

    public void putDashboard(Long userId, DashboardSummaryResponse dashboard) {
        redisTemplate.opsForValue().set(
                CacheKeys.dashboard(userId),
                dashboard,
                Duration.ofSeconds(cacheProperties.getDashboardTtlSeconds()));
    }

    public void evictDashboard(Long userId) {
        redisTemplate.delete(CacheKeys.dashboard(userId));
    }
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AnalyticsCacheService.class);
}
