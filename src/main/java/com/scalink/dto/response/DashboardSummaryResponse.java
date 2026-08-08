package com.scalink.dto.response;

import java.util.List;

public class DashboardSummaryResponse {

    private Long userId;
    private long totalLinks;
    private long totalClicks;
    private List<PopularUrlSummary> popularUrls;
    private List<AnalyticsSummaryResponse.LabelCount> topCountries;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public long getTotalLinks() { return totalLinks; }
    public void setTotalLinks(long totalLinks) { this.totalLinks = totalLinks; }
    public long getTotalClicks() { return totalClicks; }
    public void setTotalClicks(long totalClicks) { this.totalClicks = totalClicks; }
    public List<PopularUrlSummary> getPopularUrls() { return popularUrls; }
    public void setPopularUrls(List<PopularUrlSummary> popularUrls) { this.popularUrls = popularUrls; }
    public List<AnalyticsSummaryResponse.LabelCount> getTopCountries() { return topCountries; }
    public void setTopCountries(List<AnalyticsSummaryResponse.LabelCount> topCountries) { this.topCountries = topCountries; }

    public DashboardSummaryResponse() {}
    public DashboardSummaryResponse(Long userId, long totalLinks, long totalClicks, List<PopularUrlSummary> popularUrls, List<AnalyticsSummaryResponse.LabelCount> topCountries) {
        this.userId = userId; this.totalLinks = totalLinks; this.totalClicks = totalClicks; this.popularUrls = popularUrls; this.topCountries = topCountries;
    }

    public static DashboardSummaryResponseBuilder builder() { return new DashboardSummaryResponseBuilder(); }

    public static class DashboardSummaryResponseBuilder {
        private Long userId; private long totalLinks; private long totalClicks; private List<PopularUrlSummary> popularUrls; private List<AnalyticsSummaryResponse.LabelCount> topCountries;
        public DashboardSummaryResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public DashboardSummaryResponseBuilder totalLinks(long totalLinks) { this.totalLinks = totalLinks; return this; }
        public DashboardSummaryResponseBuilder totalClicks(long totalClicks) { this.totalClicks = totalClicks; return this; }
        public DashboardSummaryResponseBuilder popularUrls(List<PopularUrlSummary> popularUrls) { this.popularUrls = popularUrls; return this; }
        public DashboardSummaryResponseBuilder topCountries(List<AnalyticsSummaryResponse.LabelCount> topCountries) { this.topCountries = topCountries; return this; }
        public DashboardSummaryResponse build() { return new DashboardSummaryResponse(userId, totalLinks, totalClicks, popularUrls, topCountries); }
    }

    public static class PopularUrlSummary {
        private Long urlId;
        private String shortCode;
        private String customAlias;
        private String shortUrl;
        private long clickCount;

        public Long getUrlId() { return urlId; }
        public void setUrlId(Long urlId) { this.urlId = urlId; }
        public String getShortCode() { return shortCode; }
        public void setShortCode(String shortCode) { this.shortCode = shortCode; }
        public String getCustomAlias() { return customAlias; }
        public void setCustomAlias(String customAlias) { this.customAlias = customAlias; }
        public String getShortUrl() { return shortUrl; }
        public void setShortUrl(String shortUrl) { this.shortUrl = shortUrl; }
        public long getClickCount() { return clickCount; }
        public void setClickCount(long clickCount) { this.clickCount = clickCount; }

        public PopularUrlSummary() {}
        public PopularUrlSummary(Long urlId, String shortCode, String customAlias, String shortUrl, long clickCount) {
            this.urlId = urlId; this.shortCode = shortCode; this.customAlias = customAlias; this.shortUrl = shortUrl; this.clickCount = clickCount;
        }

        public static PopularUrlSummaryBuilder builder() { return new PopularUrlSummaryBuilder(); }

        public static class PopularUrlSummaryBuilder {
            private Long urlId; private String shortCode; private String customAlias; private String shortUrl; private long clickCount;
            public PopularUrlSummaryBuilder urlId(Long urlId) { this.urlId = urlId; return this; }
            public PopularUrlSummaryBuilder shortCode(String shortCode) { this.shortCode = shortCode; return this; }
            public PopularUrlSummaryBuilder customAlias(String customAlias) { this.customAlias = customAlias; return this; }
            public PopularUrlSummaryBuilder shortUrl(String shortUrl) { this.shortUrl = shortUrl; return this; }
            public PopularUrlSummaryBuilder clickCount(long clickCount) { this.clickCount = clickCount; return this; }
            public PopularUrlSummary build() { return new PopularUrlSummary(urlId, shortCode, customAlias, shortUrl, clickCount); }
        }
    }
}
