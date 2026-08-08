package com.scalink.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false, length = 20)
        private String role = "USER";

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
    public Long getId() { return this.id; }
    public String getUsername() { return this.username; }
    public String getEmail() { return this.email; }
    public String getPasswordHash() { return this.passwordHash; }
    public Instant getCreatedAt() { return this.createdAt; }
    public String getRole() { return this.role; }
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setRole(String role) { this.role = role; }
    public User() {}
    public User(Long id, String username, String email, String passwordHash, Instant createdAt, String role) { this.id = id; this.username = username; this.email = email; this.passwordHash = passwordHash; this.createdAt = createdAt; this.role = role; }
    public static UserBuilder builder() { return new UserBuilder(); }
    public static class UserBuilder {
        private Long id;
        public UserBuilder id(Long id) { this.id = id; return this; }
        private String username;
        public UserBuilder username(String username) { this.username = username; return this; }
        private String email;
        public UserBuilder email(String email) { this.email = email; return this; }
        private String passwordHash;
        public UserBuilder passwordHash(String passwordHash) { this.passwordHash = passwordHash; return this; }
        private Instant createdAt;
        public UserBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        private String role = "USER";
        public UserBuilder role(String role) { this.role = role; return this; }
        public User build() { return new User(id, username, email, passwordHash, createdAt, role); }
    }
}
