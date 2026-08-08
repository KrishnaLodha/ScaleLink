package com.scalink.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "scalink.rate-limit")
public class RateLimitProperties {

    private boolean enabled = true;
    private int anonymousPerMinute = 100;
    private int authenticatedPerMinute = 500;
    private int windowSeconds = 60;
    public boolean isEnabled() { return this.enabled; }
    public int getAnonymousPerMinute() { return this.anonymousPerMinute; }
    public int getAuthenticatedPerMinute() { return this.authenticatedPerMinute; }
    public int getWindowSeconds() { return this.windowSeconds; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setAnonymousPerMinute(int anonymousPerMinute) { this.anonymousPerMinute = anonymousPerMinute; }
    public void setAuthenticatedPerMinute(int authenticatedPerMinute) { this.authenticatedPerMinute = authenticatedPerMinute; }
    public void setWindowSeconds(int windowSeconds) { this.windowSeconds = windowSeconds; }
}
