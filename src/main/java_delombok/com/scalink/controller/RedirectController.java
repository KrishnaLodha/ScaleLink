package com.scalink.controller;

import com.scalink.service.RedirectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Tag(name = "Redirect", description = "Public URL redirection")
public class RedirectController {

    private final RedirectService redirectService;

    @GetMapping("/{code:[a-zA-Z0-9_-]{3,50}}")
    @Operation(summary = "Redirect to the original URL")
    public ResponseEntity<Void> redirect(
            @PathVariable String code,
            HttpServletRequest request) {
        String targetUrl = redirectService.resolveRedirect(code, request);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, targetUrl)
                .build();
    }
}
