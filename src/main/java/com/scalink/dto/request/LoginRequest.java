package com.scalink.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LoginRequest {

    @NotBlank(message = "Username or email is required")
    private String usernameOrEmail;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private String password;
    public String getUsernameOrEmail() { return this.usernameOrEmail; }
    public String getPassword() { return this.password; }
    public void setUsernameOrEmail(String usernameOrEmail) { this.usernameOrEmail = usernameOrEmail; }
    public void setPassword(String password) { this.password = password; }
    public LoginRequest() {}
    public LoginRequest(String usernameOrEmail, String password) { this.usernameOrEmail = usernameOrEmail; this.password = password; }
    public static LoginRequestBuilder builder() { return new LoginRequestBuilder(); }
    public static class LoginRequestBuilder {
        private String usernameOrEmail;
        public LoginRequestBuilder usernameOrEmail(String usernameOrEmail) { this.usernameOrEmail = usernameOrEmail; return this; }
        private String password;
        public LoginRequestBuilder password(String password) { this.password = password; return this; }
        public LoginRequest build() { return new LoginRequest(usernameOrEmail, password); }
    }
}
