package com.scalink.util;

import java.util.Set;

public final class ReservedPaths {

    private static final Set<String> RESERVED = Set.of(
            "api", "swagger-ui", "swagger-ui.html", "api-docs", "v3",
            "actuator", "favicon.ico", "error");

    private ReservedPaths() {
    }

    public static boolean isReserved(String path) {
        if (path == null) {
            return false;
        }
        String normalized = path.toLowerCase();
        return RESERVED.contains(normalized) || normalized.startsWith("api/")
                || normalized.startsWith("swagger") || normalized.startsWith("actuator");
    }
}
