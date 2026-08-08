package com.scalink.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ClickEvent {

    private Long urlId;
    private String country;
    private String browser;
    private String device;
    private String operatingSystem;
    private String referrer;
    private String ipHash;
    public Long getUrlId() { return this.urlId; }
    public String getCountry() { return this.country; }
    public String getBrowser() { return this.browser; }
    public String getDevice() { return this.device; }
    public String getOperatingSystem() { return this.operatingSystem; }
    public String getReferrer() { return this.referrer; }
    public String getIpHash() { return this.ipHash; }
    public void setUrlId(Long urlId) { this.urlId = urlId; }
    public void setCountry(String country) { this.country = country; }
    public void setBrowser(String browser) { this.browser = browser; }
    public void setDevice(String device) { this.device = device; }
    public void setOperatingSystem(String operatingSystem) { this.operatingSystem = operatingSystem; }
    public void setReferrer(String referrer) { this.referrer = referrer; }
    public void setIpHash(String ipHash) { this.ipHash = ipHash; }
    public ClickEvent() {}
    public ClickEvent(Long urlId, String country, String browser, String device, String operatingSystem, String referrer, String ipHash) { this.urlId = urlId; this.country = country; this.browser = browser; this.device = device; this.operatingSystem = operatingSystem; this.referrer = referrer; this.ipHash = ipHash; }
    public static ClickEventBuilder builder() { return new ClickEventBuilder(); }
    public static class ClickEventBuilder {
        private Long urlId;
        public ClickEventBuilder urlId(Long urlId) { this.urlId = urlId; return this; }
        private String country;
        public ClickEventBuilder country(String country) { this.country = country; return this; }
        private String browser;
        public ClickEventBuilder browser(String browser) { this.browser = browser; return this; }
        private String device;
        public ClickEventBuilder device(String device) { this.device = device; return this; }
        private String operatingSystem;
        public ClickEventBuilder operatingSystem(String operatingSystem) { this.operatingSystem = operatingSystem; return this; }
        private String referrer;
        public ClickEventBuilder referrer(String referrer) { this.referrer = referrer; return this; }
        private String ipHash;
        public ClickEventBuilder ipHash(String ipHash) { this.ipHash = ipHash; return this; }
        public ClickEvent build() { return new ClickEvent(urlId, country, browser, device, operatingSystem, referrer, ipHash); }
    }
}
