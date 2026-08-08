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
public class TopItemsResponse {

    private Long urlId;
    private String dimension;
    private List<AnalyticsSummaryResponse.LabelCount> items;
}
