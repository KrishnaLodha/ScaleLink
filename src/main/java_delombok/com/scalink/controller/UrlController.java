package com.scalink.controller;

import com.scalink.dto.request.CreateUrlRequest;
import com.scalink.dto.request.UpdateUrlRequest;
import com.scalink.dto.response.PagedResponse;
import com.scalink.dto.response.UrlResponse;
import com.scalink.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
@Tag(name = "URLs", description = "URL shortening and link management")
@SecurityRequirement(name = "bearerAuth")
public class UrlController {

    private final UrlService urlService;

    @PostMapping
    @Operation(summary = "Create a short URL")
    public ResponseEntity<UrlResponse> createUrl(@Valid @RequestBody CreateUrlRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(urlService.createUrl(request));
    }

    @GetMapping
    @Operation(summary = "List URLs for the authenticated user (paginated)")
    public ResponseEntity<PagedResponse<UrlResponse>> listUrls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(urlService.listUrls(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a URL by ID")
    public ResponseEntity<UrlResponse> getUrl(@PathVariable Long id) {
        return ResponseEntity.ok(urlService.getUrl(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a URL")
    public ResponseEntity<UrlResponse> updateUrl(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUrlRequest request) {
        return ResponseEntity.ok(urlService.updateUrl(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a URL")
    public ResponseEntity<Void> deleteUrl(@PathVariable Long id) {
        urlService.deleteUrl(id);
        return ResponseEntity.noContent().build();
    }
}
