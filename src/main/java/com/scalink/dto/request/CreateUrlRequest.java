package com.scalink.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

public class CreateUrlRequest {

    @NotBlank(message = "Original URL is required")
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
    public CreateUrlRequest() {}
    public CreateUrlRequest(String originalUrl, String customAlias, Instant expirationDate) { this.originalUrl = originalUrl; this.customAlias = customAlias; this.expirationDate = expirationDate; }
    public static CreateUrlRequestBuilder builder() { return new CreateUrlRequestBuilder(); }
    public static class CreateUrlRequestBuilder {
        private String originalUrl;
        public CreateUrlRequestBuilder originalUrl(String originalUrl) { this.originalUrl = originalUrl; return this; }
        private String customAlias;
        public CreateUrlRequestBuilder customAlias(String customAlias) { this.customAlias = customAlias; return this; }
        private Instant expirationDate;
        public CreateUrlRequestBuilder expirationDate(Instant expirationDate) { this.expirationDate = expirationDate; return this; }
        public CreateUrlRequest build() { return new CreateUrlRequest(originalUrl, customAlias, expirationDate); }
    }
}
