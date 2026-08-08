package com.scalink.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RequestMetricsFilter extends OncePerRequestFilter {

    private final Timer requestTimer;
    private final Counter errorCounter;

    public RequestMetricsFilter(MeterRegistry meterRegistry) {
        this.requestTimer = Timer.builder("scalink.http.request")
                .description("HTTP request duration")
                .register(meterRegistry);
        this.errorCounter = Counter.builder("scalink.http.errors")
                .description("HTTP error responses")
                .register(meterRegistry);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        long start = System.nanoTime();
        try {
            filterChain.doFilter(request, response);
        } finally {
            requestTimer.record(System.nanoTime() - start, java.util.concurrent.TimeUnit.NANOSECONDS);
            if (response.getStatus() >= 500) {
                errorCounter.increment();
            }
        }
    }
}
