package com.scalink.service;

import com.scalink.exception.DuplicateResourceException;
import com.scalink.repository.UrlRepository;
import com.scalink.util.Base62Encoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShortCodeGenerator {

    private static final int MAX_RETRIES = 5;

    private final UrlRepository urlRepository;

    @Value("${scalink.short-code-length:7}")
    private int shortCodeLength;

    public String generateUniqueShortCode() {
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            String candidate = Base62Encoder.randomCode(shortCodeLength);
            if (!urlRepository.existsByShortCode(candidate)
                    && !urlRepository.existsByCustomAlias(candidate)) {
                return candidate;
            }
            log.debug("Short code collision on attempt {}, retrying", attempt + 1);
        }
        throw new DuplicateResourceException("Unable to generate unique short code after retries");
    }

    public String generateFromId(long id) {
        return Base62Encoder.encode(id);
    }

    public void assertUniqueShortCode(String shortCode) {
        if (urlRepository.existsByShortCode(shortCode)) {
            throw new DuplicateResourceException("Short code already exists");
        }
    }

    public void assertUniqueCustomAlias(String alias) {
        if (alias != null && urlRepository.existsByCustomAlias(alias)) {
            throw new DuplicateResourceException("Custom alias is already taken");
        }
        if (alias != null && urlRepository.existsByShortCode(alias)) {
            throw new DuplicateResourceException("Custom alias conflicts with an existing short code");
        }
    }

    public void handleIntegrityViolation(DataIntegrityViolationException ex) {
        throw new DuplicateResourceException("Short code or custom alias already exists");
    }
}
