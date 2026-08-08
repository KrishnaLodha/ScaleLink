package com.scalink.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LabelCount {
        private String label;
        private long count;
    }
}
