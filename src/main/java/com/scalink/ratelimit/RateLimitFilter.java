package com.scalink.ratelimit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Request filter that enforces distributed rate limits before controller execution.
 * Placed after JWT authentication so tier (anonymous/authenticated/admin) is known.
 */
@org.springframework.context.annotation.Profile("!test")
public class RateLimitFilter extends org.springframework.web.filter.OncePerRequestFilter {
    public RateLimitFilter(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    private final RateLimitService rateLimitService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        rateLimitService.checkRateLimit(request);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/actuator")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/api-docs")
                || path.startsWith("/v3/api-docs");
    }
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RateLimitFilter.class);
}
