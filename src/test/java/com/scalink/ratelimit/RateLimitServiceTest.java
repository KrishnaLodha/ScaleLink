package com.scalink.ratelimit;

import com.scalink.config.RateLimitProperties;
import com.scalink.exception.RateLimitExceededException;
import com.scalink.security.SecurityUser;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RateLimitServiceTest {

    @Mock
    private RedisTokenBucketService tokenBucketService;

    @Mock
    private HttpServletRequest request;

    private RateLimitService rateLimitService;

    @BeforeEach
    void setUp() {
        RateLimitProperties properties = new RateLimitProperties();
        properties.setEnabled(true);
        properties.setAnonymousPerMinute(100);
        properties.setAuthenticatedPerMinute(500);
        properties.setWindowSeconds(60);
        rateLimitService = new RateLimitService(tokenBucketService, properties, new SimpleMeterRegistry());
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void checkRateLimit_shouldAllowWhenTokensAvailable() {
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(tokenBucketService.tryConsume(anyString(), anyInt(), anyInt())).thenReturn(true);

        rateLimitService.checkRateLimit(request);
    }

    @Test
    void checkRateLimit_shouldThrow429WhenExceeded() {
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(tokenBucketService.tryConsume(anyString(), anyInt(), anyInt())).thenReturn(false);

        assertThatThrownBy(() -> rateLimitService.checkRateLimit(request))
                .isInstanceOf(RateLimitExceededException.class);
    }

    @Test
    void checkRateLimit_shouldSkipForAdmin() {
        SecurityUser admin = new SecurityUser(
                com.scalink.entity.User.builder()
                        .id(1L)
                        .username("admin")
                        .email("admin@example.com")
                        .passwordHash("hash")
                        .role("ADMIN")
                        .build());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(admin, null, admin.getAuthorities()));

        rateLimitService.checkRateLimit(request);
    }
}
