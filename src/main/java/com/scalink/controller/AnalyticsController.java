package com.scalink.controller;

import com.scalink.dto.response.AnalyticsSummaryResponse;
import com.scalink.dto.response.DailyClicksResponse;
import com.scalink.dto.response.TopItemsResponse;
import com.scalink.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "Click analytics and reporting")
@SecurityRequirement(name = "bearerAuth")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/{urlId}")
    @Operation(summary = "Get analytics summary for a URL")
    public ResponseEntity<AnalyticsSummaryResponse> getSummary(@PathVariable Long urlId) {
        return ResponseEntity.ok(analyticsService.getSummary(urlId));
    }

    @GetMapping("/{urlId}/daily")
    @Operation(summary = "Get daily click counts for a URL")
    public ResponseEntity<DailyClicksResponse> getDailyClicks(@PathVariable Long urlId) {
        return ResponseEntity.ok(analyticsService.getDailyClicks(urlId));
    }

    @GetMapping("/{urlId}/countries")
    @Operation(summary = "Get top countries for a URL")
    public ResponseEntity<TopItemsResponse> getTopCountries(@PathVariable Long urlId) {
        return ResponseEntity.ok(analyticsService.getTopCountries(urlId));
    }

    @GetMapping("/{urlId}/browsers")
    @Operation(summary = "Get top browsers for a URL")
    public ResponseEntity<TopItemsResponse> getTopBrowsers(@PathVariable Long urlId) {
        return ResponseEntity.ok(analyticsService.getTopBrowsers(urlId));
    }

    @GetMapping("/{urlId}/devices")
    @Operation(summary = "Get top devices for a URL")
    public ResponseEntity<TopItemsResponse> getTopDevices(@PathVariable Long urlId) {
        return ResponseEntity.ok(analyticsService.getTopDevices(urlId));
    }

    @GetMapping("/{urlId}/referrers")
    @Operation(summary = "Get top referrers for a URL")
    public ResponseEntity<TopItemsResponse> getTopReferrers(@PathVariable Long urlId) {
        return ResponseEntity.ok(analyticsService.getTopReferrers(urlId));
    }
}
