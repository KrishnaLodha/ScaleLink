package com.scalink.dto.response;

import com.scalink.entity.Url;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

public class UrlResponse {

    private Long id;
    private String originalUrl;
    private String shortCode;
    private String customAlias;
    private String shortUrl;
    private Long clickCount;
    private Instant expirationDate;
    private Instant createdAt;
    private boolean expired;

    public static UrlResponse fromEntity(Url url, String baseUrl) {
        String redirectKey = url.getRedirectKey();
        return UrlResponse.builder()
                .id(url.getId())
                .originalUrl(url.getOriginalUrl())
                .shortCode(url.getShortCode())
                .customAlias(url.getCustomAlias())
                .shortUrl(baseUrl + "/" + redirectKey)
                .clickCount(url.getClickCount())
                .expirationDate(url.getExpirationDate())
                .createdAt(url.getCreatedAt())
                .expired(url.isExpired())
                .build();
    }
    public Long getId() { return this.id; }
    public String getOriginalUrl() { return this.originalUrl; }
    public String getShortCode() { return this.shortCode; }
    public String getCustomAlias() { return this.customAlias; }
    public String getShortUrl() { return this.shortUrl; }
    public Long getClickCount() { return this.clickCount; }
    public Instant getExpirationDate() { return this.expirationDate; }
    public Instant getCreatedAt() { return this.createdAt; }
    public boolean getExpired() { return this.expired; }
    public void setId(Long id) { this.id = id; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }
    public void setShortCode(String shortCode) { this.shortCode = shortCode; }
    public void setCustomAlias(String customAlias) { this.customAlias = customAlias; }
    public void setShortUrl(String shortUrl) { this.shortUrl = shortUrl; }
    public void setClickCount(Long clickCount) { this.clickCount = clickCount; }
    public void setExpirationDate(Instant expirationDate) { this.expirationDate = expirationDate; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setExpired(boolean expired) { this.expired = expired; }
    public UrlResponse() {}
    public UrlResponse(Long id, String originalUrl, String shortCode, String customAlias, String shortUrl, Long clickCount, Instant expirationDate, Instant createdAt, boolean expired) { this.id = id; this.originalUrl = originalUrl; this.shortCode = shortCode; this.customAlias = customAlias; this.shortUrl = shortUrl; this.clickCount = clickCount; this.expirationDate = expirationDate; this.createdAt = createdAt; this.expired = expired; }
    public static UrlResponseBuilder builder() { return new UrlResponseBuilder(); }
    public static class UrlResponseBuilder {
        private Long id;
        public UrlResponseBuilder id(Long id) { this.id = id; return this; }
        private String originalUrl;
        public UrlResponseBuilder originalUrl(String originalUrl) { this.originalUrl = originalUrl; return this; }
        private String shortCode;
        public UrlResponseBuilder shortCode(String shortCode) { this.shortCode = shortCode; return this; }
        private String customAlias;
        public UrlResponseBuilder customAlias(String customAlias) { this.customAlias = customAlias; return this; }
        private String shortUrl;
        public UrlResponseBuilder shortUrl(String shortUrl) { this.shortUrl = shortUrl; return this; }
        private Long clickCount;
        public UrlResponseBuilder clickCount(Long clickCount) { this.clickCount = clickCount; return this; }
        private Instant expirationDate;
        public UrlResponseBuilder expirationDate(Instant expirationDate) { this.expirationDate = expirationDate; return this; }
        private Instant createdAt;
        public UrlResponseBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        private boolean expired;
        public UrlResponseBuilder expired(boolean expired) { this.expired = expired; return this; }
        public UrlResponse build() { return new UrlResponse(id, originalUrl, shortCode, customAlias, shortUrl, clickCount, expirationDate, createdAt, expired); }
    }
}
