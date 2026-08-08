package com.scalink.dto.response;

import java.util.List;
import java.util.Map;

public class AnalyticsSummaryResponse {

    private Long urlId;
    private long totalClicks;
    private long dailyClicks;
    private long weeklyClicks;
    private long monthlyClicks;
    private List<LabelCount> topCountries;
    private List<LabelCount> topBrowsers;
    private List<LabelCount> topDevices;
    private List<LabelCount> topReferrers;

    public Long getUrlId() { return urlId; }
    public void setUrlId(Long urlId) { this.urlId = urlId; }
    public long getTotalClicks() { return totalClicks; }
    public void setTotalClicks(long totalClicks) { this.totalClicks = totalClicks; }
    public long getDailyClicks() { return dailyClicks; }
    public void setDailyClicks(long dailyClicks) { this.dailyClicks = dailyClicks; }
    public long getWeeklyClicks() { return weeklyClicks; }
    public void setWeeklyClicks(long weeklyClicks) { this.weeklyClicks = weeklyClicks; }
    public long getMonthlyClicks() { return monthlyClicks; }
    public void setMonthlyClicks(long monthlyClicks) { this.monthlyClicks = monthlyClicks; }
    public List<LabelCount> getTopCountries() { return topCountries; }
    public void setTopCountries(List<LabelCount> topCountries) { this.topCountries = topCountries; }
    public List<LabelCount> getTopBrowsers() { return topBrowsers; }
    public void setTopBrowsers(List<LabelCount> topBrowsers) { this.topBrowsers = topBrowsers; }
    public List<LabelCount> getTopDevices() { return topDevices; }
    public void setTopDevices(List<LabelCount> topDevices) { this.topDevices = topDevices; }
    public List<LabelCount> getTopReferrers() { return topReferrers; }
    public void setTopReferrers(List<LabelCount> topReferrers) { this.topReferrers = topReferrers; }

    public AnalyticsSummaryResponse() {}
    public AnalyticsSummaryResponse(Long urlId, long totalClicks, long dailyClicks, long weeklyClicks, long monthlyClicks, List<LabelCount> topCountries, List<LabelCount> topBrowsers, List<LabelCount> topDevices, List<LabelCount> topReferrers) {
        this.urlId = urlId; this.totalClicks = totalClicks; this.dailyClicks = dailyClicks; this.weeklyClicks = weeklyClicks; this.monthlyClicks = monthlyClicks; this.topCountries = topCountries; this.topBrowsers = topBrowsers; this.topDevices = topDevices; this.topReferrers = topReferrers;
    }

    public static AnalyticsSummaryResponseBuilder builder() { return new AnalyticsSummaryResponseBuilder(); }

    public static class AnalyticsSummaryResponseBuilder {
        private Long urlId; private long totalClicks; private long dailyClicks; private long weeklyClicks; private long monthlyClicks; private List<LabelCount> topCountries; private List<LabelCount> topBrowsers; private List<LabelCount> topDevices; private List<LabelCount> topReferrers;
        public AnalyticsSummaryResponseBuilder urlId(Long urlId) { this.urlId = urlId; return this; }
        public AnalyticsSummaryResponseBuilder totalClicks(long totalClicks) { this.totalClicks = totalClicks; return this; }
        public AnalyticsSummaryResponseBuilder dailyClicks(long dailyClicks) { this.dailyClicks = dailyClicks; return this; }
        public AnalyticsSummaryResponseBuilder weeklyClicks(long weeklyClicks) { this.weeklyClicks = weeklyClicks; return this; }
        public AnalyticsSummaryResponseBuilder monthlyClicks(long monthlyClicks) { this.monthlyClicks = monthlyClicks; return this; }
        public AnalyticsSummaryResponseBuilder topCountries(List<LabelCount> topCountries) { this.topCountries = topCountries; return this; }
        public AnalyticsSummaryResponseBuilder topBrowsers(List<LabelCount> topBrowsers) { this.topBrowsers = topBrowsers; return this; }
        public AnalyticsSummaryResponseBuilder topDevices(List<LabelCount> topDevices) { this.topDevices = topDevices; return this; }
        public AnalyticsSummaryResponseBuilder topReferrers(List<LabelCount> topReferrers) { this.topReferrers = topReferrers; return this; }
        public AnalyticsSummaryResponse build() { return new AnalyticsSummaryResponse(urlId, totalClicks, dailyClicks, weeklyClicks, monthlyClicks, topCountries, topBrowsers, topDevices, topReferrers); }
    }

    public static class LabelCount {
        private String label;
        private long count;

        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
        public long getCount() { return count; }
        public void setCount(long count) { this.count = count; }

        public LabelCount() {}
        public LabelCount(String label, long count) { this.label = label; this.count = count; }

        public static LabelCountBuilder builder() { return new LabelCountBuilder(); }

        public static class LabelCountBuilder {
            private String label; private long count;
            public LabelCountBuilder label(String label) { this.label = label; return this; }
            public LabelCountBuilder count(long count) { this.count = count; return this; }
            public LabelCount build() { return new LabelCount(label, count); }
        }
    }
}
