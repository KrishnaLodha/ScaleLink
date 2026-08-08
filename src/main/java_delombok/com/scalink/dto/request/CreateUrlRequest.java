package com.scalink.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUrlRequest {

    @NotBlank(message = "Original URL is required")
    @Size(max = 2048, message = "URL must not exceed 2048 characters")
    private String originalUrl;

    @Size(min = 3, max = 50, message = "Custom alias must be between 3 and 50 characters")
    private String customAlias;

    @Future(message = "Expiration date must be in the future")
    private Instant expirationDate;
}
