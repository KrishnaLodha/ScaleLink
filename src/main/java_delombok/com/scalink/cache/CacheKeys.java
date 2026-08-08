package com.scalink.cache;

public final class CacheKeys {

    public static final String URL_REDIRECT_PREFIX = "url:redirect:";
    public static final String ANALYTICS_SUMMARY_PREFIX = "analytics:summary:";
    public static final String DASHBOARD_PREFIX = "dashboard:user:";

    private CacheKeys() {
    }

    public static String urlRedirect(String code) {
        return URL_REDIRECT_PREFIX + code;
    }

    public static String analyticsSummary(Long urlId) {
        return ANALYTICS_SUMMARY_PREFIX + urlId;
    }

    public static String dashboard(Long userId) {
        return DASHBOARD_PREFIX + userId;
    }
}
