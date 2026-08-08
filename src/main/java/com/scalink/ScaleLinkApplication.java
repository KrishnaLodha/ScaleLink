package com.scalink;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableScheduling
@EnableRetry
@EnableConfigurationProperties({
        com.scalink.config.CacheProperties.class,
        com.scalink.config.RateLimitProperties.class
})
public class ScaleLinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScaleLinkApplication.class, args);
    }
}
