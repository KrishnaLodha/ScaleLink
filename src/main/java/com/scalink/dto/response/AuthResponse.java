package com.scalink.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthResponse {

    private String accessToken;
    private String tokenType;
    private long expiresInMs;
    private UserResponse user;
    public String getAccessToken() { return this.accessToken; }
    public String getTokenType() { return this.tokenType; }
    public long getExpiresInMs() { return this.expiresInMs; }
    public UserResponse getUser() { return this.user; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    public void setExpiresInMs(long expiresInMs) { this.expiresInMs = expiresInMs; }
    public void setUser(UserResponse user) { this.user = user; }
    public AuthResponse() {}
    public AuthResponse(String accessToken, String tokenType, long expiresInMs, UserResponse user) { this.accessToken = accessToken; this.tokenType = tokenType; this.expiresInMs = expiresInMs; this.user = user; }
    public static AuthResponseBuilder builder() { return new AuthResponseBuilder(); }
    public static class AuthResponseBuilder {
        private String accessToken;
        public AuthResponseBuilder accessToken(String accessToken) { this.accessToken = accessToken; return this; }
        private String tokenType;
        public AuthResponseBuilder tokenType(String tokenType) { this.tokenType = tokenType; return this; }
        private long expiresInMs;
        public AuthResponseBuilder expiresInMs(long expiresInMs) { this.expiresInMs = expiresInMs; return this; }
        private UserResponse user;
        public AuthResponseBuilder user(UserResponse user) { this.user = user; return this; }
        public AuthResponse build() { return new AuthResponse(accessToken, tokenType, expiresInMs, user); }
    }
}
