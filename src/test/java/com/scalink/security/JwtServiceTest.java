package com.scalink.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;
    private SecurityUser securityUser;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(
                "test-secret-key-must-be-at-least-256-bits-long-for-hs256-algorithm",
                3600000L);

        securityUser = new SecurityUser(
                com.scalink.entity.User.builder()
                        .id(42L)
                        .username("jwtuser")
                        .email("jwt@example.com")
                        .passwordHash("hash")
                        .build());
    }

    @Test
    void generateToken_shouldProduceValidToken() {
        String token = jwtService.generateToken(securityUser);

        assertThat(token).isNotBlank();
        assertThat(jwtService.extractUsername(token)).isEqualTo("jwtuser");
        assertThat(jwtService.extractUserId(token)).isEqualTo(42L);
        assertThat(jwtService.isTokenValid(token, securityUser)).isTrue();
    }

    @Test
    void isTokenValid_shouldReturnFalseForWrongUser() {
        String token = jwtService.generateToken(securityUser);
        SecurityUser otherUser = new SecurityUser(
                com.scalink.entity.User.builder()
                        .id(99L)
                        .username("otheruser")
                        .email("other@example.com")
                        .passwordHash("hash")
                        .build());

        assertThat(jwtService.isTokenValid(token, otherUser)).isFalse();
    }

    @Test
    void getExpirationMs_shouldReturnConfiguredValue() {
        assertThat(jwtService.getExpirationMs()).isEqualTo(3600000L);
    }
}
