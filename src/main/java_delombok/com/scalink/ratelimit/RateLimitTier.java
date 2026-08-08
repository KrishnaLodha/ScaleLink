package com.scalink.ratelimit;

public enum RateLimitTier {

    ANONYMOUS(100),
    AUTHENTICATED(500),
    ADMIN(Integer.MAX_VALUE);

    private final int requestsPerMinute;

    RateLimitTier(int requestsPerMinute) {
        this.requestsPerMinute = requestsPerMinute;
    }

    public int getRequestsPerMinute() {
        return requestsPerMinute;
    }
}
