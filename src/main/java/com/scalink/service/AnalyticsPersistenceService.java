package com.scalink.service;

import com.scalink.dto.event.ClickEvent;
import com.scalink.entity.Analytics;
import com.scalink.entity.Url;
import com.scalink.exception.ResourceNotFoundException;
import com.scalink.repository.AnalyticsRepository;
import com.scalink.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsPersistenceService {

    private final AnalyticsRepository analyticsRepository;
    private final UrlRepository urlRepository;

    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 500, multiplier = 2))
    @Transactional
    public void persistClick(ClickEvent event) {
        Url url = urlRepository.findById(event.getUrlId())
                .orElseThrow(() -> new ResourceNotFoundException("URL not found"));

        Analytics analytics = Analytics.builder()
                .url(url)
                .country(event.getCountry())
                .browser(event.getBrowser())
                .device(event.getDevice())
                .operatingSystem(event.getOperatingSystem())
                .referrer(event.getReferrer())
                .ipHash(event.getIpHash())
                .build();

        analyticsRepository.save(analytics);
        url.setClickCount(url.getClickCount() + 1);
        urlRepository.save(url);
    }

    @Recover
    public void recoverPersistClick(Exception ex, ClickEvent event) {
        log.error("Analytics write failed after retries for urlId={}", event.getUrlId(), ex);
    }
}
