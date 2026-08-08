package com.scalink.dto.response;

import com.scalink.entity.Url;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlResponse {

    private Long id;
    private String originalUrl;
    private String shortCode;
    private String customAlias;
    private String shortUrl;
    private Long clickCount;
    private Instant expirationDate;
    private Instant createdAt;
    private boolean expired;

    public static UrlResponse fromEntity(Url url, String baseUrl) {
        String redirectKey = url.getRedirectKey();
        return UrlResponse.builder()
                .id(url.getId())
                .originalUrl(url.getOriginalUrl())
                .shortCode(url.getShortCode())
                .customAlias(url.getCustomAlias())
                .shortUrl(baseUrl + "/" + redirectKey)
                .clickCount(url.getClickCount())
                .expirationDate(url.getExpirationDate())
                .createdAt(url.getCreatedAt())
                .expired(url.isExpired())
                .build();
    }
}
