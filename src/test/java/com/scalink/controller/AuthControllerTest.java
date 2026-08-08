package com.scalink.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalink.dto.request.LoginRequest;
import com.scalink.dto.request.RegisterRequest;
import com.scalink.dto.response.AuthResponse;
import com.scalink.dto.response.UserResponse;
import com.scalink.exception.GlobalExceptionHandler;
import com.scalink.service.AuthService;
import com.scalink.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    @Test
    void register_shouldReturn201() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .username("newuser")
                .email("new@example.com")
                .password("password123")
                .build();

        AuthResponse response = AuthResponse.builder()
                .accessToken("token")
                .tokenType("Bearer")
                .expiresInMs(86400000L)
                .user(UserResponse.builder()
                        .id(1L)
                        .username("newuser")
                        .email("new@example.com")
                        .createdAt(Instant.now())
                        .build())
                .build();

        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").value("token"))
                .andExpect(jsonPath("$.user.username").value("newuser"));
    }

    @Test
    void register_shouldReturn400OnValidationFailure() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .username("ab")
                .email("invalid-email")
                .password("short")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.username").exists())
                .andExpect(jsonPath("$.validationErrors.email").exists())
                .andExpect(jsonPath("$.validationErrors.password").exists());
    }

    @Test
    void login_shouldReturn200() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .usernameOrEmail("user")
                .password("password123")
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(
                AuthResponse.builder()
                        .accessToken("token")
                        .tokenType("Bearer")
                        .expiresInMs(86400000L)
                        .build());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("token"));
    }

    @Test
    void getCurrentUser_shouldReturnUserProfile() throws Exception {
        when(userService.getCurrentUser()).thenReturn(
                UserResponse.builder()
                        .id(1L)
                        .username("user")
                        .email("user@example.com")
                        .createdAt(Instant.now())
                        .build());

        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user"));
    }
}
