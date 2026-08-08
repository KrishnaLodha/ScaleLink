package com.scalink.service;

import com.scalink.cache.UrlCacheService;
import com.scalink.dto.cache.UrlCacheEntry;
import com.scalink.dto.event.ClickEvent;
import com.scalink.entity.Url;
import com.scalink.exception.ResourceNotFoundException;
import com.scalink.exception.UrlExpiredException;
import com.scalink.util.IpHasher;
import com.scalink.util.ParsedUserAgent;
import com.scalink.util.ReservedPaths;
import com.scalink.util.UserAgentParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RedirectService {

    private final UrlService urlService;
    private final AnalyticsService analyticsService;

    @Autowired(required = false)
    private UrlCacheService urlCacheService;

    public String resolveRedirect(String code, HttpServletRequest request) {
        if (ReservedPaths.isReserved(code)) {
            throw new ResourceNotFoundException("Short URL not found");
        }

        UrlCacheEntry cached = lookupCache(code);
        if (cached != null) {
            validateNotExpired(cached);
            publishAnalytics(cached.getUrlId(), request);
            return cached.getOriginalUrl();
        }

        Url url = urlService.findByCodeOrAlias(code);
        validateNotExpired(url);
        urlService.cacheUrl(url);
        publishAnalytics(url.getId(), request);
        return url.getOriginalUrl();
    }

    private UrlCacheEntry lookupCache(String code) {
        if (urlCacheService == null) {
            return null;
        }
        return urlCacheService.get(code).orElse(null);
    }

    private void validateNotExpired(UrlCacheEntry entry) {
        if (entry.isExpired()) {
            if (urlCacheService != null) {
                urlCacheService.evict(entry);
            }
            throw new UrlExpiredException("This short URL has expired");
        }
    }

    private void validateNotExpired(Url url) {
        if (url.isExpired()) {
            urlService.invalidateCache(
                    UrlCacheEntry.builder()
                            .shortCode(url.getShortCode())
                            .customAlias(url.getCustomAlias())
                            .build(),
                    null);
            throw new UrlExpiredException("This short URL has expired");
        }
    }

    private void publishAnalytics(Long urlId, HttpServletRequest request) {
        ParsedUserAgent parsed = UserAgentParser.parse(request.getHeader("User-Agent"));
        ClickEvent event = ClickEvent.builder()
                .urlId(urlId)
                .country(request.getHeader("CF-IPCountry"))
                .browser(parsed.browser())
                .device(parsed.device())
                .operatingSystem(parsed.operatingSystem())
                .referrer(request.getHeader("Referer"))
                .ipHash(IpHasher.hash(resolveClientIp(request)))
                .build();
        analyticsService.recordClickAsync(event);
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
