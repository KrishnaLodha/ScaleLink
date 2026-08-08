package com.scalink.service;

import com.scalink.cache.AnalyticsCacheService;
import com.scalink.dto.response.AnalyticsSummaryResponse;
import com.scalink.dto.response.DashboardSummaryResponse;
import com.scalink.entity.Url;
import com.scalink.repository.AnalyticsRepository;
import com.scalink.repository.UrlRepository;
import com.scalink.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UrlRepository urlRepository;
    private final AnalyticsRepository analyticsRepository;
    private final UserService userService;

    @Autowired(required = false)
    private AnalyticsCacheService analyticsCacheService;

    @Value("${scalink.base-url}")
    private String baseUrl;

    @Transactional(readOnly = true)
    public DashboardSummaryResponse getDashboard() {
        SecurityUser currentUser = userService.getAuthenticatedUser();

        if (analyticsCacheService != null) {
            var cached = analyticsCacheService.getDashboard(currentUser.getId());
            if (cached.isPresent()) {
                return cached.get();
            }
        }

        DashboardSummaryResponse dashboard = buildDashboard(currentUser.getId());
        if (analyticsCacheService != null) {
            analyticsCacheService.putDashboard(currentUser.getId(), dashboard);
        }
        return dashboard;
    }

    private DashboardSummaryResponse buildDashboard(Long userId) {
        List<Url> popularUrls = urlRepository.findTop5ByUser_IdOrderByClickCountDesc(userId);

        return DashboardSummaryResponse.builder()
                .userId(userId)
                .totalLinks(urlRepository.countByUser_Id(userId))
                .totalClicks(urlRepository.sumClickCountByUserId(userId))
                .popularUrls(popularUrls.stream()
                        .map(url -> DashboardSummaryResponse.PopularUrlSummary.builder()
                                .urlId(url.getId())
                                .shortCode(url.getShortCode())
                                .customAlias(url.getCustomAlias())
                                .shortUrl(baseUrl + "/" + url.getRedirectKey())
                                .clickCount(url.getClickCount())
                                .build())
                        .toList())
                .topCountries(analyticsRepository.findTopCountriesByUser(userId, PageRequest.of(0, 10)).stream()
                        .map(row -> AnalyticsSummaryResponse.LabelCount.builder()
                                .label(String.valueOf(row[0]))
                                .count(((Number) row[1]).longValue())
                                .build())
                        .toList())
                .build();
    }
}
