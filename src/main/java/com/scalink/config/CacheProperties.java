package com.scalink.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "scalink.cache")
public class CacheProperties {

    private long urlTtlSeconds = 86400;
    private long analyticsTtlSeconds = 900;
    private long dashboardTtlSeconds = 300;
}
