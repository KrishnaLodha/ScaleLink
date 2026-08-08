package com.scalink.service;

import com.scalink.cache.AnalyticsCacheService;
import com.scalink.dto.event.ClickEvent;
import com.scalink.dto.response.AnalyticsSummaryResponse;
import com.scalink.dto.response.DailyClicksResponse;
import com.scalink.dto.response.TopItemsResponse;
import com.scalink.entity.Url;
import com.scalink.exception.ForbiddenOperationException;
import com.scalink.exception.ResourceNotFoundException;
import com.scalink.repository.AnalyticsRepository;
import com.scalink.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private static final int TOP_LIMIT = 10;
    private static final int DAILY_LIMIT = 30;

    private final AnalyticsRepository analyticsRepository;
    private final UrlRepository urlRepository;
    private final UserService userService;
    private final AnalyticsPersistenceService analyticsPersistenceService;

    @Autowired(required = false)
    private AnalyticsCacheService analyticsCacheService;

    @Async("analyticsExecutor")
    public void recordClickAsync(ClickEvent event) {
        try {
            analyticsPersistenceService.persistClick(event);
            evictCachesAfterClick(event.getUrlId());
        } catch (Exception ex) {
            log.error("Async analytics failed for urlId={}: {}", event.getUrlId(), ex.getMessage());
        }
    }

    private void evictCachesAfterClick(Long urlId) {
        if (analyticsCacheService == null) {
            return;
        }
        analyticsCacheService.evictSummary(urlId);
        urlRepository.findById(urlId).ifPresent(url -> {
            if (url.getUser() != null) {
                analyticsCacheService.evictDashboard(url.getUser().getId());
            }
        });
    }

    @Transactional(readOnly = true)
    public AnalyticsSummaryResponse getSummary(Long urlId) {
        assertUrlAccess(urlId);

        if (analyticsCacheService != null) {
            var cached = analyticsCacheService.getSummary(urlId);
            if (cached.isPresent()) {
                return cached.get();
            }
        }

        AnalyticsSummaryResponse summary = buildSummary(urlId);
        if (analyticsCacheService != null) {
            analyticsCacheService.putSummary(urlId, summary);
        }
        return summary;
    }

    @Transactional(readOnly = true)
    public DailyClicksResponse getDailyClicks(Long urlId) {
        assertUrlAccess(urlId);
        List<DailyClicksResponse.DailyCount> dailyCounts = analyticsRepository
                .findDailyClicks(urlId, DAILY_LIMIT).stream()
                .map(row -> DailyClicksResponse.DailyCount.builder()
                        .date(((java.sql.Date) row[0]).toLocalDate())
                        .clicks(((Number) row[1]).longValue())
                        .build())
                .toList();

        return DailyClicksResponse.builder()
                .urlId(urlId)
                .dailyCounts(dailyCounts)
                .build();
    }

    @Transactional(readOnly = true)
    public TopItemsResponse getTopCountries(Long urlId) {
        return buildTopItems(urlId, "countries",
                analyticsRepository.findTopCountries(urlId, PageRequest.of(0, TOP_LIMIT)));
    }

    @Transactional(readOnly = true)
    public TopItemsResponse getTopBrowsers(Long urlId) {
        return buildTopItems(urlId, "browsers",
                analyticsRepository.findTopBrowsers(urlId, PageRequest.of(0, TOP_LIMIT)));
    }

    @Transactional(readOnly = true)
    public TopItemsResponse getTopDevices(Long urlId) {
        return buildTopItems(urlId, "devices",
                analyticsRepository.findTopDevices(urlId, PageRequest.of(0, TOP_LIMIT)));
    }

    @Transactional(readOnly = true)
    public TopItemsResponse getTopReferrers(Long urlId) {
        return buildTopItems(urlId, "referrers",
                analyticsRepository.findTopReferrers(urlId, PageRequest.of(0, TOP_LIMIT)));
    }

    private AnalyticsSummaryResponse buildSummary(Long urlId) {
        Instant now = Instant.now();
        return AnalyticsSummaryResponse.builder()
                .urlId(urlId)
                .totalClicks(analyticsRepository.countByUrl_Id(urlId))
                .dailyClicks(analyticsRepository.countByUrlIdSince(urlId, now.minus(1, ChronoUnit.DAYS)))
                .weeklyClicks(analyticsRepository.countByUrlIdSince(urlId, now.minus(7, ChronoUnit.DAYS)))
                .monthlyClicks(analyticsRepository.countByUrlIdSince(urlId, now.minus(30, ChronoUnit.DAYS)))
                .topCountries(mapTopItems(analyticsRepository.findTopCountries(urlId, PageRequest.of(0, TOP_LIMIT))))
                .topBrowsers(mapTopItems(analyticsRepository.findTopBrowsers(urlId, PageRequest.of(0, TOP_LIMIT))))
                .topDevices(mapTopItems(analyticsRepository.findTopDevices(urlId, PageRequest.of(0, TOP_LIMIT))))
                .topReferrers(mapTopItems(analyticsRepository.findTopReferrers(urlId, PageRequest.of(0, TOP_LIMIT))))
                .build();
    }

    private TopItemsResponse buildTopItems(Long urlId, String dimension, List<Object[]> rows) {
        assertUrlAccess(urlId);
        return TopItemsResponse.builder()
                .urlId(urlId)
                .dimension(dimension)
                .items(mapTopItems(rows))
                .build();
    }

    private List<AnalyticsSummaryResponse.LabelCount> mapTopItems(List<Object[]> rows) {
        return rows.stream()
                .map(row -> AnalyticsSummaryResponse.LabelCount.builder()
                        .label(String.valueOf(row[0]))
                        .count(((Number) row[1]).longValue())
                        .build())
                .toList();
    }

    private void assertUrlAccess(Long urlId) {
        Url url = urlRepository.findById(urlId)
                .orElseThrow(() -> new ResourceNotFoundException("URL not found"));
        if (url.getUser() == null
                || !url.getUser().getId().equals(userService.getAuthenticatedUser().getId())) {
            throw new ForbiddenOperationException("You do not have permission to view analytics for this URL");
        }
    }
}
