package com.scalink.config;

import com.scalink.cache.UrlCacheService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class CacheMetricsConfig {

    @Autowired(required = false)
    private UrlCacheService urlCacheService;

    private final MeterRegistry meterRegistry;

    public CacheMetricsConfig(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void registerGauges() {
        if (urlCacheService != null) {
            Gauge.builder("scalink.cache.hit.ratio", urlCacheService, UrlCacheService::getHitRatio)
                    .description("URL cache hit ratio")
                    .register(meterRegistry);
        }
    }
}
