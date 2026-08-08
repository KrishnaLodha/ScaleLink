package com.scalink.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UrlValidatorTest {

    @Test
    void normalizeUrl_shouldAcceptHttpsUrl() {
        assertThat(UrlValidator.normalizeUrl("https://example.com/path"))
                .isEqualTo("https://example.com/path");
    }

    @Test
    void normalizeUrl_shouldAddHttpsWhenMissing() {
        assertThat(UrlValidator.normalizeUrl("example.com"))
                .isEqualTo("https://example.com");
    }

    @Test
    void normalizeUrl_shouldRejectInvalidScheme() {
        assertThatThrownBy(() -> UrlValidator.normalizeUrl("ftp://example.com"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validateCustomAlias_shouldRejectReservedAlias() {
        assertThatThrownBy(() -> UrlValidator.validateCustomAlias("api"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validateCustomAlias_shouldAcceptValidAlias() {
        UrlValidator.validateCustomAlias("my-link");
    }
}
