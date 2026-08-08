package com.scalink.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

public class UpdateUrlRequest {

    @Size(max = 2048, message = "URL must not exceed 2048 characters")
    private String originalUrl;

    @Size(min = 3, max = 50, message = "Custom alias must be between 3 and 50 characters")
    private String customAlias;

    @Future(message = "Expiration date must be in the future")
    private Instant expirationDate;
    public String getOriginalUrl() { return this.originalUrl; }
    public String getCustomAlias() { return this.customAlias; }
    public Instant getExpirationDate() { return this.expirationDate; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }
    public void setCustomAlias(String customAlias) { this.customAlias = customAlias; }
    public void setExpirationDate(Instant expirationDate) { this.expirationDate = expirationDate; }
    public UpdateUrlRequest() {}
    public UpdateUrlRequest(String originalUrl, String customAlias, Instant expirationDate) { this.originalUrl = originalUrl; this.customAlias = customAlias; this.expirationDate = expirationDate; }
    public static UpdateUrlRequestBuilder builder() { return new UpdateUrlRequestBuilder(); }
    public static class UpdateUrlRequestBuilder {
        private String originalUrl;
        public UpdateUrlRequestBuilder originalUrl(String originalUrl) { this.originalUrl = originalUrl; return this; }
        private String customAlias;
        public UpdateUrlRequestBuilder customAlias(String customAlias) { this.customAlias = customAlias; return this; }
        private Instant expirationDate;
        public UpdateUrlRequestBuilder expirationDate(Instant expirationDate) { this.expirationDate = expirationDate; return this; }
        public UpdateUrlRequest build() { return new UpdateUrlRequest(originalUrl, customAlias, expirationDate); }
    }
}
