package com.scalink.dto.response;

import com.scalink.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private Instant createdAt;

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }
    public Long getId() { return this.id; }
    public String getUsername() { return this.username; }
    public String getEmail() { return this.email; }
    public Instant getCreatedAt() { return this.createdAt; }
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public UserResponse() {}
    public UserResponse(Long id, String username, String email, Instant createdAt) { this.id = id; this.username = username; this.email = email; this.createdAt = createdAt; }
    public static UserResponseBuilder builder() { return new UserResponseBuilder(); }
    public static class UserResponseBuilder {
        private Long id;
        public UserResponseBuilder id(Long id) { this.id = id; return this; }
        private String username;
        public UserResponseBuilder username(String username) { this.username = username; return this; }
        private String email;
        public UserResponseBuilder email(String email) { this.email = email; return this; }
        private Instant createdAt;
        public UserResponseBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public UserResponse build() { return new UserResponse(id, username, email, createdAt); }
    }
}
