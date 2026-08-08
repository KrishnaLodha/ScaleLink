package com.scalink.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "scalink.rate-limit")
public class RateLimitProperties {

    private boolean enabled = true;
    private int anonymousPerMinute = 100;
    private int authenticatedPerMinute = 500;
    private int windowSeconds = 60;
}
