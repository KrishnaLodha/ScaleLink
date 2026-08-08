package com.scalink.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RegisterRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private String password;
    public String getUsername() { return this.username; }
    public String getEmail() { return this.email; }
    public String getPassword() { return this.password; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public RegisterRequest() {}
    public RegisterRequest(String username, String email, String password) { this.username = username; this.email = email; this.password = password; }
    public static RegisterRequestBuilder builder() { return new RegisterRequestBuilder(); }
    public static class RegisterRequestBuilder {
        private String username;
        public RegisterRequestBuilder username(String username) { this.username = username; return this; }
        private String email;
        public RegisterRequestBuilder email(String email) { this.email = email; return this; }
        private String password;
        public RegisterRequestBuilder password(String password) { this.password = password; return this; }
        public RegisterRequest build() { return new RegisterRequest(username, email, password); }
    }
}
