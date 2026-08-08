package com.scalink.dto.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlCacheEntry implements Serializable {

    private Long urlId;
    private String originalUrl;
    private Instant expirationDate;
    private String shortCode;
    private String customAlias;

    public boolean isExpired() {
        return expirationDate != null && Instant.now().isAfter(expirationDate);
    }
}
