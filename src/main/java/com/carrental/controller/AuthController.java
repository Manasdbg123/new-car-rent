package com.carrental.controller;

import com.carrental.dto.request.LoginRequest;
import com.carrental.dto.request.RegisterRequest;
import com.carrental.dto.response.AuthResponse;
import com.carrental.service.AuthService;
import com.carrental.service.EmailService; // <-- Imported EmailService

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

	private final AuthService authService;
	private final EmailService emailService; // <-- Injected EmailService

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(
			@RequestBody RegisterRequest request
	) {
		// 1. Create the user in the database
		AuthResponse response = authService.register(request);

		// 2. Trigger the Welcome Email asynchronously (in the background)
		emailService.sendWelcomeEmail(request.getEmail(), request.getFullName());

		// 3. Return the token to the frontend
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(
			@RequestBody LoginRequest request
	) {
		return ResponseEntity.ok(
				authService.login(request)
		);
	}
}