package com.carrental.controller;

import com.carrental.dto.request.LoginRequest;
import com.carrental.dto.request.RegisterRequest;
import com.carrental.dto.response.AuthResponse;
import com.carrental.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(
			@RequestBody RegisterRequest request
	) {

		return ResponseEntity.ok(
				authService.register(request)
		);
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