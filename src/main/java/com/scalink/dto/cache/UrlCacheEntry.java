package com.scalink.dto.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

public class UrlCacheEntry implements Serializable {

    private Long urlId;
    private String originalUrl;
    private Instant expirationDate;
    private String shortCode;
    private String customAlias;

    public boolean isExpired() {
        return expirationDate != null && Instant.now().isAfter(expirationDate);
    }
    public Long getUrlId() { return this.urlId; }
    public String getOriginalUrl() { return this.originalUrl; }
    public Instant getExpirationDate() { return this.expirationDate; }
    public String getShortCode() { return this.shortCode; }
    public String getCustomAlias() { return this.customAlias; }
    public void setUrlId(Long urlId) { this.urlId = urlId; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }
    public void setExpirationDate(Instant expirationDate) { this.expirationDate = expirationDate; }
    public void setShortCode(String shortCode) { this.shortCode = shortCode; }
    public void setCustomAlias(String customAlias) { this.customAlias = customAlias; }
    public UrlCacheEntry() {}
    public UrlCacheEntry(Long urlId, String originalUrl, Instant expirationDate, String shortCode, String customAlias) { this.urlId = urlId; this.originalUrl = originalUrl; this.expirationDate = expirationDate; this.shortCode = shortCode; this.customAlias = customAlias; }
    public static UrlCacheEntryBuilder builder() { return new UrlCacheEntryBuilder(); }
    public static class UrlCacheEntryBuilder {
        private Long urlId;
        public UrlCacheEntryBuilder urlId(Long urlId) { this.urlId = urlId; return this; }
        private String originalUrl;
        public UrlCacheEntryBuilder originalUrl(String originalUrl) { this.originalUrl = originalUrl; return this; }
        private Instant expirationDate;
        public UrlCacheEntryBuilder expirationDate(Instant expirationDate) { this.expirationDate = expirationDate; return this; }
        private String shortCode;
        public UrlCacheEntryBuilder shortCode(String shortCode) { this.shortCode = shortCode; return this; }
        private String customAlias;
        public UrlCacheEntryBuilder customAlias(String customAlias) { this.customAlias = customAlias; return this; }
        public UrlCacheEntry build() { return new UrlCacheEntry(urlId, originalUrl, expirationDate, shortCode, customAlias); }
    }
}
