package com.scalink.config;

import com.scalink.ratelimit.RateLimitFilter;
import com.scalink.ratelimit.RateLimitService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class RateLimitConfig {

    @Bean
    public RateLimitFilter rateLimitFilter(RateLimitService rateLimitService) {
        return new RateLimitFilter(rateLimitService);
    }
}
