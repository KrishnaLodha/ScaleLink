package com.scalink.service;

import com.scalink.dto.request.CreateUrlRequest;
import com.scalink.dto.response.UrlResponse;
import com.scalink.entity.Url;
import com.scalink.entity.User;
import com.scalink.exception.ForbiddenOperationException;
import com.scalink.repository.UrlRepository;
import com.scalink.security.SecurityUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private ShortCodeGenerator shortCodeGenerator;

    @Mock
    private UserService userService;

    @InjectMocks
    private UrlService urlService;

    private SecurityUser securityUser;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(urlService, "baseUrl", "http://localhost:8080");
        securityUser = new SecurityUser(User.builder()
                .id(1L)
                .username("alice")
                .email("alice@example.com")
                .passwordHash("hash")
                .build());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities()));
        when(userService.getAuthenticatedUser()).thenReturn(securityUser);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createUrl_shouldPersistAndReturnResponse() {
        CreateUrlRequest request = CreateUrlRequest.builder()
                .originalUrl("https://example.com")
                .customAlias("my-link")
                .build();

        when(shortCodeGenerator.generateUniqueShortCode()).thenReturn("abc1234");
        when(urlRepository.save(any(Url.class))).thenAnswer(invocation -> {
            Url url = invocation.getArgument(0);
            url.setId(10L);
            url.setCreatedAt(Instant.now());
            return url;
        });

        UrlResponse response = urlService.createUrl(request);

        assertThat(response.getShortCode()).isEqualTo("abc1234");
        assertThat(response.getCustomAlias()).isEqualTo("my-link");
        assertThat(response.getShortUrl()).isEqualTo("http://localhost:8080/my-link");
        verify(urlRepository).save(any(Url.class));
    }

    @Test
    void getUrl_shouldThrowWhenNotOwner() {
        Url url = Url.builder()
                .id(5L)
                .shortCode("xyz")
                .originalUrl("https://example.com")
                .user(User.builder().id(99L).build())
                .build();

        when(urlRepository.findById(5L)).thenReturn(Optional.of(url));

        assertThatThrownBy(() -> urlService.getUrl(5L))
                .isInstanceOf(ForbiddenOperationException.class);
    }
}
