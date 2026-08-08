package com.scalink.ratelimit;

import com.scalink.config.RateLimitProperties;
import com.scalink.exception.RateLimitExceededException;
import com.scalink.security.SecurityUser;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("!test")
public class RateLimitService {

    private static final String RATE_KEY_PREFIX = "rate:bucket:";

    private final RedisTokenBucketService tokenBucketService;
    private final RateLimitProperties properties;
    private final Counter rateLimitExceededCounter;

    @Autowired
    public RateLimitService(
            RedisTokenBucketService tokenBucketService,
            RateLimitProperties properties,
            MeterRegistry meterRegistry) {
        this.tokenBucketService = tokenBucketService;
        this.properties = properties;
        this.rateLimitExceededCounter = meterRegistry.counter("scalink.rate_limit.exceeded");
    }

    public void checkRateLimit(HttpServletRequest request) {
        if (!properties.isEnabled()) {
            return;
        }

        RateLimitTier tier = resolveTier();
        if (tier == RateLimitTier.ADMIN) {
            return;
        }

        String bucketKey = buildBucketKey(request, tier);
        int capacity = tier == RateLimitTier.AUTHENTICATED
                ? properties.getAuthenticatedPerMinute()
                : properties.getAnonymousPerMinute();

        boolean allowed = tokenBucketService.tryConsume(
                bucketKey,
                capacity,
                properties.getWindowSeconds());

        if (!allowed) {
            rateLimitExceededCounter.increment();
            log.warn("Rate limit exceeded for key={} tier={}", bucketKey, tier);
            throw new RateLimitExceededException("Rate limit exceeded. Please retry later.");
        }
    }

    private RateLimitTier resolveTier() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof SecurityUser) {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch("ROLE_ADMIN"::equals);
            return isAdmin ? RateLimitTier.ADMIN : RateLimitTier.AUTHENTICATED;
        }
        return RateLimitTier.ANONYMOUS;
    }

    private String buildBucketKey(HttpServletRequest request, RateLimitTier tier) {
        if (tier == RateLimitTier.AUTHENTICATED) {
            SecurityUser user = (SecurityUser) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            return RATE_KEY_PREFIX + "user:" + user.getId();
        }
        return RATE_KEY_PREFIX + "ip:" + resolveClientIp(request);
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
