package com.carrental.controller;

import com.carrental.dto.request.LoginRequest;
import com.carrental.dto.request.RefreshTokenRequest;
import com.carrental.dto.request.RegisterRequest;
import com.carrental.dto.response.ApiResponse;
import com.carrental.dto.response.AuthResponse;
import com.carrental.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<AuthResponse>> register(
			@Valid @RequestBody RegisterRequest request
	) {

		AuthResponse response = authService.register(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(
						ApiResponse.<AuthResponse>builder()
								.success(true)
								.message("User registered successfully")
								.data(response)
								.timestamp(LocalDateTime.now())
								.build()
				);
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<AuthResponse>> login(
			@Valid @RequestBody LoginRequest request
	) {

		AuthResponse response = authService.login(request);

		return ResponseEntity.ok(
				ApiResponse.<AuthResponse>builder()
						.success(true)
						.message("Login successful")
						.data(response)
						.timestamp(LocalDateTime.now())
						.build()
		);
	}

	@PostMapping("/refresh")
	public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
			@Valid @RequestBody RefreshTokenRequest request
	) {

		AuthResponse response = authService.refreshToken(request);

		return ResponseEntity.ok(
				ApiResponse.<AuthResponse>builder()
						.success(true)
						.message("Token refreshed successfully")
						.data(response)
						.timestamp(LocalDateTime.now())
						.build()
		);
	}
}