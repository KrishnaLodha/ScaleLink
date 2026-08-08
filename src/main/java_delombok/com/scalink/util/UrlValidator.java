package com.scalink.util;

import java.net.IDN;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.regex.Pattern;

public final class UrlValidator {

    private static final Pattern ALIAS_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{3,50}$");
    private static final Set<String> ALLOWED_SCHEMES = Set.of("http", "https");

    private UrlValidator() {
    }

    public static void validateOriginalUrl(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL must not be blank");
        }
        try {
            URI uri = new URI(url.trim());
            if (uri.getScheme() == null || uri.getHost() == null) {
                URI normalized = new URI("https://" + url.trim());
                validateUri(normalized);
                return;
            }
            validateUri(uri);
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Invalid URL format");
        }
    }

    public static String normalizeUrl(String url) {
        validateOriginalUrl(url);
        try {
            URI uri = new URI(url.trim());
            if (uri.getScheme() == null) {
                uri = new URI("https://" + url.trim());
            }
            String host = IDN.toASCII(uri.getHost());
            String normalized = new URI(
                    uri.getScheme().toLowerCase(),
                    uri.getUserInfo(),
                    host,
                    uri.getPort(),
                    uri.getPath(),
                    uri.getQuery(),
                    uri.getFragment()).toString();
            return normalized;
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Invalid URL format");
        }
    }

    public static void validateCustomAlias(String alias) {
        if (alias == null || alias.isBlank()) {
            return;
        }
        if (!ALIAS_PATTERN.matcher(alias).matches()) {
            throw new IllegalArgumentException(
                    "Custom alias must be 3-50 characters and contain only letters, numbers, hyphens, and underscores");
        }
        if (ReservedPaths.isReserved(alias)) {
            throw new IllegalArgumentException("Custom alias is reserved");
        }
    }

    private static void validateUri(URI uri) throws URISyntaxException {
        if (!ALLOWED_SCHEMES.contains(uri.getScheme().toLowerCase())) {
            throw new IllegalArgumentException("URL must use http or https scheme");
        }
        if (uri.getHost() == null || uri.getHost().isBlank()) {
            throw new IllegalArgumentException("URL must have a valid host");
        }
    }
}
