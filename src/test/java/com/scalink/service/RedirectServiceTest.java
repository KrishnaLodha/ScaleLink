package com.scalink.service;

import com.scalink.dto.cache.UrlCacheEntry;
import com.scalink.dto.event.ClickEvent;
import com.scalink.entity.Url;
import com.scalink.exception.UrlExpiredException;
import com.scalink.util.ReservedPaths;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedirectServiceTest {

    @Mock
    private UrlService urlService;

    @Mock
    private AnalyticsService analyticsService;

    @InjectMocks
    private RedirectService redirectService;

    @Test
    void resolveRedirect_shouldReturnOriginalUrlFromDatabase() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Url url = Url.builder()
                .id(1L)
                .shortCode("abc1234")
                .originalUrl("https://example.com")
                .build();

        when(urlService.findByCodeOrAlias("abc1234")).thenReturn(url);
        doNothing().when(analyticsService).recordClickAsync(any(ClickEvent.class));

        String result = redirectService.resolveRedirect("abc1234", request);

        assertThat(result).isEqualTo("https://example.com");
        verify(urlService).cacheUrl(url);
        verify(analyticsService).recordClickAsync(any(ClickEvent.class));
    }

    @Test
    void resolveRedirect_shouldThrowWhenExpired() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Url url = Url.builder()
                .id(1L)
                .shortCode("expired")
                .originalUrl("https://example.com")
                .expirationDate(Instant.now().minus(1, ChronoUnit.DAYS))
                .build();

        when(urlService.findByCodeOrAlias("expired")).thenReturn(url);

        assertThatThrownBy(() -> redirectService.resolveRedirect("expired", request))
                .isInstanceOf(UrlExpiredException.class);

        verify(analyticsService, never()).recordClickAsync(any());
    }

    @Test
    void reservedPaths_shouldBlockApiPath() {
        assertThat(ReservedPaths.isReserved("api")).isTrue();
        assertThat(ReservedPaths.isReserved("swagger-ui")).isTrue();
    }
}
