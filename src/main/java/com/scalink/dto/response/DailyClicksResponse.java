package com.scalink.dto.response;

import java.time.LocalDate;
import java.util.List;

public class DailyClicksResponse {

    private Long urlId;
    private List<DailyCount> dailyCounts;

    public Long getUrlId() { return urlId; }
    public void setUrlId(Long urlId) { this.urlId = urlId; }
    public List<DailyCount> getDailyCounts() { return dailyCounts; }
    public void setDailyCounts(List<DailyCount> dailyCounts) { this.dailyCounts = dailyCounts; }

    public DailyClicksResponse() {}
    public DailyClicksResponse(Long urlId, List<DailyCount> dailyCounts) { this.urlId = urlId; this.dailyCounts = dailyCounts; }

    public static DailyClicksResponseBuilder builder() { return new DailyClicksResponseBuilder(); }

    public static class DailyClicksResponseBuilder {
        private Long urlId; private List<DailyCount> dailyCounts;
        public DailyClicksResponseBuilder urlId(Long urlId) { this.urlId = urlId; return this; }
        public DailyClicksResponseBuilder dailyCounts(List<DailyCount> dailyCounts) { this.dailyCounts = dailyCounts; return this; }
        public DailyClicksResponse build() { return new DailyClicksResponse(urlId, dailyCounts); }
    }

    public static class DailyCount {
        private LocalDate date;
        private long clicks;

        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }
        public long getClicks() { return clicks; }
        public void setClicks(long clicks) { this.clicks = clicks; }

        public DailyCount() {}
        public DailyCount(LocalDate date, long clicks) { this.date = date; this.clicks = clicks; }

        public static DailyCountBuilder builder() { return new DailyCountBuilder(); }

        public static class DailyCountBuilder {
            private LocalDate date; private long clicks;
            public DailyCountBuilder date(LocalDate date) { this.date = date; return this; }
            public DailyCountBuilder clicks(long clicks) { this.clicks = clicks; return this; }
            public DailyCount build() { return new DailyCount(date, clicks); }
        }
    }
}
