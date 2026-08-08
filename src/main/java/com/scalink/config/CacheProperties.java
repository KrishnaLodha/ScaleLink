package com.scalink.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "scalink.cache")
public class CacheProperties {

    private long urlTtlSeconds = 86400;
    private long analyticsTtlSeconds = 900;
    private long dashboardTtlSeconds = 300;
    public long getUrlTtlSeconds() { return this.urlTtlSeconds; }
    public long getAnalyticsTtlSeconds() { return this.analyticsTtlSeconds; }
    public long getDashboardTtlSeconds() { return this.dashboardTtlSeconds; }
    public void setUrlTtlSeconds(long urlTtlSeconds) { this.urlTtlSeconds = urlTtlSeconds; }
    public void setAnalyticsTtlSeconds(long analyticsTtlSeconds) { this.analyticsTtlSeconds = analyticsTtlSeconds; }
    public void setDashboardTtlSeconds(long dashboardTtlSeconds) { this.dashboardTtlSeconds = dashboardTtlSeconds; }
}
