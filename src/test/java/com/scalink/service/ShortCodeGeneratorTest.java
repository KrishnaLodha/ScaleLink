package com.scalink.service;

import com.scalink.exception.DuplicateResourceException;
import com.scalink.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShortCodeGeneratorTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private ShortCodeGenerator shortCodeGenerator;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(shortCodeGenerator, "shortCodeLength", 7);
    }

    @Test
    void generateUniqueShortCode_shouldReturnCodeWhenNoCollision() {
        when(urlRepository.existsByShortCode(anyString())).thenReturn(false);
        when(urlRepository.existsByCustomAlias(anyString())).thenReturn(false);

        String code = shortCodeGenerator.generateUniqueShortCode();

        assertThat(code).hasSize(7);
    }

    @Test
    void generateUniqueShortCode_shouldRetryOnCollision() {
        when(urlRepository.existsByShortCode(anyString()))
                .thenReturn(true, false);
        when(urlRepository.existsByCustomAlias(anyString())).thenReturn(false);

        String code = shortCodeGenerator.generateUniqueShortCode();

        assertThat(code).hasSize(7);
        verify(urlRepository, atLeastOnce()).existsByShortCode(anyString());
    }

    @Test
    void assertUniqueCustomAlias_shouldThrowWhenTaken() {
        when(urlRepository.existsByCustomAlias("taken")).thenReturn(true);

        assertThatThrownBy(() -> shortCodeGenerator.assertUniqueCustomAlias("taken"))
                .isInstanceOf(DuplicateResourceException.class);
    }
}
