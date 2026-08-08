package com.scalink.service;

import com.scalink.cache.AnalyticsCacheService;
import com.scalink.cache.UrlCacheService;
import com.scalink.dto.cache.UrlCacheEntry;
import com.scalink.dto.request.CreateUrlRequest;
import com.scalink.dto.request.UpdateUrlRequest;
import com.scalink.dto.response.PagedResponse;
import com.scalink.dto.response.UrlResponse;
import com.scalink.entity.Url;
import com.scalink.entity.User;
import com.scalink.exception.ForbiddenOperationException;
import com.scalink.exception.InvalidUrlException;
import com.scalink.exception.ResourceNotFoundException;
import com.scalink.repository.UrlRepository;
import com.scalink.security.SecurityUser;
import com.scalink.util.UrlValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final UserService userService;

    @Autowired(required = false)
    private UrlCacheService urlCacheService;

    @Autowired(required = false)
    private AnalyticsCacheService analyticsCacheService;

    @Value("${scalink.base-url}")
    private String baseUrl;

    @Transactional
    public UrlResponse createUrl(CreateUrlRequest request) {
        SecurityUser currentUser = userService.getAuthenticatedUser();
        String normalizedUrl = normalizeUrl(request.getOriginalUrl());
        UrlValidator.validateCustomAlias(request.getCustomAlias());
        shortCodeGenerator.assertUniqueCustomAlias(request.getCustomAlias());

        String shortCode = shortCodeGenerator.generateUniqueShortCode();

        User user = User.builder().id(currentUser.getId()).build();
        Url url = Url.builder()
                .originalUrl(normalizedUrl)
                .shortCode(shortCode)
                .customAlias(request.getCustomAlias())
                .user(user)
                .expirationDate(request.getExpirationDate())
                .build();

        try {
            Url saved = urlRepository.save(url);
            cacheUrl(saved);
            evictDashboard(currentUser.getId());
            return UrlResponse.fromEntity(saved, baseUrl);
        } catch (DataIntegrityViolationException ex) {
            shortCodeGenerator.handleIntegrityViolation(ex);
            throw ex;
        }
    }

    @Transactional(readOnly = true)
    public UrlResponse getUrl(Long id) {
        Url url = findUrlForCurrentUser(id);
        return UrlResponse.fromEntity(url, baseUrl);
    }

    @Transactional(readOnly = true)
    public PagedResponse<UrlResponse> listUrls(int page, int size) {
        SecurityUser currentUser = userService.getAuthenticatedUser();
        int safeSize = Math.min(Math.max(size, 1), 100);
        Page<Url> urlPage = urlRepository.findByUser_IdOrderByCreatedAtDesc(
                currentUser.getId(), PageRequest.of(page, safeSize));

        return PagedResponse.<UrlResponse>builder()
                .content(urlPage.getContent().stream()
                        .map(url -> UrlResponse.fromEntity(url, baseUrl))
                        .toList())
                .page(urlPage.getNumber())
                .size(urlPage.getSize())
                .totalElements(urlPage.getTotalElements())
                .totalPages(urlPage.getTotalPages())
                .hasNext(urlPage.hasNext())
                .build();
    }

    @Transactional
    public UrlResponse updateUrl(Long id, UpdateUrlRequest request) {
        Url url = findUrlForCurrentUser(id);
        UrlCacheEntry oldCacheEntry = toCacheEntry(url);

        if (request.getOriginalUrl() != null) {
            url.setOriginalUrl(normalizeUrl(request.getOriginalUrl()));
        }
        if (request.getCustomAlias() != null) {
            UrlValidator.validateCustomAlias(request.getCustomAlias());
            if (!request.getCustomAlias().equals(url.getCustomAlias())) {
                shortCodeGenerator.assertUniqueCustomAlias(request.getCustomAlias());
                url.setCustomAlias(request.getCustomAlias());
            }
        }
        if (request.getExpirationDate() != null) {
            url.setExpirationDate(request.getExpirationDate());
        }

        try {
            Url updated = urlRepository.save(url);
            invalidateCache(oldCacheEntry, updated);
            evictAnalytics(updated.getId());
            evictDashboard(userService.getAuthenticatedUser().getId());
            return UrlResponse.fromEntity(updated, baseUrl);
        } catch (DataIntegrityViolationException ex) {
            shortCodeGenerator.handleIntegrityViolation(ex);
            throw ex;
        }
    }

    @Transactional
    public void deleteUrl(Long id) {
        Url url = findUrlForCurrentUser(id);
        invalidateCache(toCacheEntry(url), null);
        evictAnalytics(url.getId());
        evictDashboard(userService.getAuthenticatedUser().getId());
        urlRepository.delete(url);
    }

    @Transactional(readOnly = true)
    public Url findByCodeOrAlias(String code) {
        return urlRepository.findByCodeOrAlias(code)
                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));
    }

    public void cacheUrl(Url url) {
        if (urlCacheService == null) {
            return;
        }
        UrlCacheEntry entry = toCacheEntry(url);
        urlCacheService.put(url.getShortCode(), entry);
        if (url.getCustomAlias() != null) {
            urlCacheService.put(url.getCustomAlias(), entry);
        }
    }

    public void invalidateCache(UrlCacheEntry oldEntry, Url updated) {
        if (urlCacheService == null) {
            return;
        }
        urlCacheService.evict(oldEntry);
        if (updated != null) {
            if (updated.isExpired()) {
                urlCacheService.evict(toCacheEntry(updated));
            } else {
                cacheUrl(updated);
            }
        }
    }

    private Url findUrlForCurrentUser(Long id) {
        SecurityUser currentUser = userService.getAuthenticatedUser();
        Url url = urlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("URL not found"));
        if (url.getUser() == null || !url.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenOperationException("You do not have permission to access this URL");
        }
        return url;
    }

    private String normalizeUrl(String url) {
        try {
            return UrlValidator.normalizeUrl(url);
        } catch (IllegalArgumentException ex) {
            throw new InvalidUrlException(ex.getMessage());
        }
    }

    private UrlCacheEntry toCacheEntry(Url url) {
        return UrlCacheEntry.builder()
                .urlId(url.getId())
                .originalUrl(url.getOriginalUrl())
                .expirationDate(url.getExpirationDate())
                .shortCode(url.getShortCode())
                .customAlias(url.getCustomAlias())
                .build();
    }

    private void evictAnalytics(Long urlId) {
        if (analyticsCacheService != null) {
            analyticsCacheService.evictSummary(urlId);
        }
    }

    private void evictDashboard(Long userId) {
        if (analyticsCacheService != null) {
            analyticsCacheService.evictDashboard(userId);
        }
    }
}
