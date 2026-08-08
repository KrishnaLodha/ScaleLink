package com.scalink.service;

import com.scalink.dto.event.ClickEvent;
import com.scalink.entity.Url;
import com.scalink.repository.AnalyticsRepository;
import com.scalink.repository.UrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsPersistenceServiceTest {

    @Mock
    private AnalyticsRepository analyticsRepository;

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private AnalyticsPersistenceService analyticsPersistenceService;

    @Test
    void persistClick_shouldSaveAnalyticsAndIncrementCount() {
        Url url = Url.builder().id(1L).clickCount(5L).build();
        when(urlRepository.findById(1L)).thenReturn(Optional.of(url));

        ClickEvent event = ClickEvent.builder()
                .urlId(1L)
                .browser("Chrome")
                .device("Desktop")
                .operatingSystem("Windows")
                .ipHash("abc123")
                .build();

        analyticsPersistenceService.persistClick(event);

        verify(analyticsRepository).save(any());
        verify(urlRepository).save(url);
    }
}
