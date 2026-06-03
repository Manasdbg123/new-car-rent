package com.carrental.controller;

import com.carrental.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/kyc")
@RequiredArgsConstructor
@Slf4j
public class KycController {

    // Simulating a database column for KYC Status (UNVERIFIED, PENDING, APPROVED)
    // In a real app, you would add a 'kycStatus' String to your User entity.
    private final Map<String, String> userKycDatabase = new ConcurrentHashMap<>();

    @GetMapping("/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> getKycStatus(Principal principal) {
        String status = userKycDatabase.getOrDefault(principal.getName(), "UNVERIFIED");

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .data(status)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/upload")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> uploadKycDocument(
            @RequestParam("document") MultipartFile file,
            Principal principal) {

        log.info("Received KYC document from {}: {} ({} bytes)",
                principal.getName(), file.getOriginalFilename(), file.getSize());

        // Here you would normally upload the file to AWS S3 or a local folder.
        // For now, we accept the file and update their status to APPROVED for immediate testing.
        userKycDatabase.put(principal.getName(), "APPROVED");

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("Document verified successfully.")
                .data("APPROVED")
                .timestamp(LocalDateTime.now())
                .build());
    }
}