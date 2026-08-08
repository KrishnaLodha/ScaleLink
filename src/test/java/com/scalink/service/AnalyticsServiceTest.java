package com.scalink.service;

import com.scalink.dto.event.ClickEvent;
import com.scalink.entity.Url;
import com.scalink.entity.User;
import com.scalink.exception.ForbiddenOperationException;
import com.scalink.repository.AnalyticsRepository;
import com.scalink.repository.UrlRepository;
import com.scalink.security.SecurityUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private AnalyticsRepository analyticsRepository;

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private UserService userService;

    @Mock
    private AnalyticsPersistenceService analyticsPersistenceService;

    @InjectMocks
    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp() {
        SecurityUser user = new SecurityUser(User.builder()
                .id(1L)
                .username("alice")
                .email("alice@example.com")
                .passwordHash("hash")
                .build());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
        when(userService.getAuthenticatedUser()).thenReturn(user);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getSummary_shouldAggregateMetrics() {
        Url url = Url.builder().id(5L).user(User.builder().id(1L).build()).build();
        when(urlRepository.findById(5L)).thenReturn(Optional.of(url));
        when(analyticsRepository.countByUrl_Id(5L)).thenReturn(100L);
        when(analyticsRepository.countByUrlIdSince(eq(5L), any(Instant.class))).thenReturn(10L, 50L, 80L);
        when(analyticsRepository.findTopCountries(eq(5L), any(Pageable.class))).thenReturn(List.of());
        when(analyticsRepository.findTopBrowsers(eq(5L), any(Pageable.class))).thenReturn(List.of());
        when(analyticsRepository.findTopDevices(eq(5L), any(Pageable.class))).thenReturn(List.of());
        when(analyticsRepository.findTopReferrers(eq(5L), any(Pageable.class))).thenReturn(List.of());

        var summary = analyticsService.getSummary(5L);

        assertThat(summary.getTotalClicks()).isEqualTo(100);
        assertThat(summary.getDailyClicks()).isEqualTo(10);
    }

    @Test
    void getSummary_shouldDenyForeignUrl() {
        Url url = Url.builder().id(5L).user(User.builder().id(99L).build()).build();
        when(urlRepository.findById(5L)).thenReturn(Optional.of(url));

        assertThatThrownBy(() -> analyticsService.getSummary(5L))
                .isInstanceOf(ForbiddenOperationException.class);
    }

    @Test
    void recordClickAsync_shouldDelegateToPersistence() {
        ClickEvent event = ClickEvent.builder().urlId(1L).browser("Chrome").build();

        analyticsService.recordClickAsync(event);

        verify(analyticsPersistenceService).persistClick(event);
    }
}
