package com.scalink.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "urls")
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_url", nullable = false, columnDefinition = "TEXT")
    private String originalUrl;

    @Column(name = "short_code", nullable = false, unique = true, length = 10)
    private String shortCode;

    @Column(name = "custom_alias", unique = true, length = 50)
    private String customAlias;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "click_count", nullable = false)
        private Long clickCount = 0L;

    @Column(name = "expiration_date")
    private Instant expirationDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (clickCount == null) {
            clickCount = 0L;
        }
    }

    public boolean isExpired() {
        return expirationDate != null && Instant.now().isAfter(expirationDate);
    }

    public String getRedirectKey() {
        return customAlias != null ? customAlias : shortCode;
    }
    public Long getId() { return this.id; }
    public String getOriginalUrl() { return this.originalUrl; }
    public String getShortCode() { return this.shortCode; }
    public String getCustomAlias() { return this.customAlias; }
    public User getUser() { return this.user; }
    public Long getClickCount() { return this.clickCount; }
    public Instant getExpirationDate() { return this.expirationDate; }
    public Instant getCreatedAt() { return this.createdAt; }
    public void setId(Long id) { this.id = id; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }
    public void setShortCode(String shortCode) { this.shortCode = shortCode; }
    public void setCustomAlias(String customAlias) { this.customAlias = customAlias; }
    public void setUser(User user) { this.user = user; }
    public void setClickCount(Long clickCount) { this.clickCount = clickCount; }
    public void setExpirationDate(Instant expirationDate) { this.expirationDate = expirationDate; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Url() {}
    public Url(Long id, String originalUrl, String shortCode, String customAlias, User user, Long clickCount, Instant expirationDate, Instant createdAt) { this.id = id; this.originalUrl = originalUrl; this.shortCode = shortCode; this.customAlias = customAlias; this.user = user; this.clickCount = clickCount; this.expirationDate = expirationDate; this.createdAt = createdAt; }
    public static UrlBuilder builder() { return new UrlBuilder(); }
    public static class UrlBuilder {
        private Long id;
        public UrlBuilder id(Long id) { this.id = id; return this; }
        private String originalUrl;
        public UrlBuilder originalUrl(String originalUrl) { this.originalUrl = originalUrl; return this; }
        private String shortCode;
        public UrlBuilder shortCode(String shortCode) { this.shortCode = shortCode; return this; }
        private String customAlias;
        public UrlBuilder customAlias(String customAlias) { this.customAlias = customAlias; return this; }
        private User user;
        public UrlBuilder user(User user) { this.user = user; return this; }
        private Long clickCount;
        public UrlBuilder clickCount(Long clickCount) { this.clickCount = clickCount; return this; }
        private Instant expirationDate;
        public UrlBuilder expirationDate(Instant expirationDate) { this.expirationDate = expirationDate; return this; }
        private Instant createdAt;
        public UrlBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Url build() { return new Url(id, originalUrl, shortCode, customAlias, user, clickCount, expirationDate, createdAt); }
    }
}
