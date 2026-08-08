package com.scalink.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class TopItemsResponse {

    private Long urlId;
    private String dimension;
    private List<AnalyticsSummaryResponse.LabelCount> items;
    public Long getUrlId() { return this.urlId; }
    public String getDimension() { return this.dimension; }
    public List<AnalyticsSummaryResponse.LabelCount> getItems() { return this.items; }
    public void setUrlId(Long urlId) { this.urlId = urlId; }
    public void setDimension(String dimension) { this.dimension = dimension; }
    public void setItems(List<AnalyticsSummaryResponse.LabelCount> items) { this.items = items; }
    public TopItemsResponse() {}
    public TopItemsResponse(Long urlId, String dimension, List<AnalyticsSummaryResponse.LabelCount> items) { this.urlId = urlId; this.dimension = dimension; this.items = items; }
    public static TopItemsResponseBuilder builder() { return new TopItemsResponseBuilder(); }
    public static class TopItemsResponseBuilder {
        private Long urlId;
        public TopItemsResponseBuilder urlId(Long urlId) { this.urlId = urlId; return this; }
        private String dimension;
        public TopItemsResponseBuilder dimension(String dimension) { this.dimension = dimension; return this; }
        private List<AnalyticsSummaryResponse.LabelCount> items;
        public TopItemsResponseBuilder items(List<AnalyticsSummaryResponse.LabelCount> items) { this.items = items; return this; }
        public TopItemsResponse build() { return new TopItemsResponse(urlId, dimension, items); }
    }
}
