package com.scalink.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {

    private Long userId;
    private long totalLinks;
    private long totalClicks;
    private List<PopularUrlSummary> popularUrls;
    private List<AnalyticsSummaryResponse.LabelCount> topCountries;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PopularUrlSummary {
        private Long urlId;
        private String shortCode;
        private String customAlias;
        private String shortUrl;
        private long clickCount;
    }
}
