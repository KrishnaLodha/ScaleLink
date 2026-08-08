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
@Table(name = "analytics")
public class Analytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "url_id", nullable = false)
    private Url url;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(length = 2)
    private String country;

    @Column(length = 50)
    private String browser;

    @Column(length = 50)
    private String device;

    @Column(name = "operating_system", length = 50)
    private String operatingSystem;

    @Column(columnDefinition = "TEXT")
    private String referrer;

    @Column(name = "ip_hash", length = 64)
    private String ipHash;

    @PrePersist
    void onCreate() {
        if (timestamp == null) {
            timestamp = Instant.now();
        }
    }
    public Long getId() { return this.id; }
    public Url getUrl() { return this.url; }
    public Instant getTimestamp() { return this.timestamp; }
    public String getCountry() { return this.country; }
    public String getBrowser() { return this.browser; }
    public String getDevice() { return this.device; }
    public String getOperatingSystem() { return this.operatingSystem; }
    public String getReferrer() { return this.referrer; }
    public String getIpHash() { return this.ipHash; }
    public void setId(Long id) { this.id = id; }
    public void setUrl(Url url) { this.url = url; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public void setCountry(String country) { this.country = country; }
    public void setBrowser(String browser) { this.browser = browser; }
    public void setDevice(String device) { this.device = device; }
    public void setOperatingSystem(String operatingSystem) { this.operatingSystem = operatingSystem; }
    public void setReferrer(String referrer) { this.referrer = referrer; }
    public void setIpHash(String ipHash) { this.ipHash = ipHash; }
    public Analytics() {}
    public Analytics(Long id, Url url, Instant timestamp, String country, String browser, String device, String operatingSystem, String referrer, String ipHash) { this.id = id; this.url = url; this.timestamp = timestamp; this.country = country; this.browser = browser; this.device = device; this.operatingSystem = operatingSystem; this.referrer = referrer; this.ipHash = ipHash; }
    public static AnalyticsBuilder builder() { return new AnalyticsBuilder(); }
    public static class AnalyticsBuilder {
        private Long id;
        public AnalyticsBuilder id(Long id) { this.id = id; return this; }
        private Url url;
        public AnalyticsBuilder url(Url url) { this.url = url; return this; }
        private Instant timestamp;
        public AnalyticsBuilder timestamp(Instant timestamp) { this.timestamp = timestamp; return this; }
        private String country;
        public AnalyticsBuilder country(String country) { this.country = country; return this; }
        private String browser;
        public AnalyticsBuilder browser(String browser) { this.browser = browser; return this; }
        private String device;
        public AnalyticsBuilder device(String device) { this.device = device; return this; }
        private String operatingSystem;
        public AnalyticsBuilder operatingSystem(String operatingSystem) { this.operatingSystem = operatingSystem; return this; }
        private String referrer;
        public AnalyticsBuilder referrer(String referrer) { this.referrer = referrer; return this; }
        private String ipHash;
        public AnalyticsBuilder ipHash(String ipHash) { this.ipHash = ipHash; return this; }
        public Analytics build() { return new Analytics(id, url, timestamp, country, browser, device, operatingSystem, referrer, ipHash); }
    }
}
