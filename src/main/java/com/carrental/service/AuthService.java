package com.carrental.service;

import com.carrental.dto.request.LoginRequest;
import com.carrental.dto.request.RefreshTokenRequest;
import com.carrental.dto.request.RegisterRequest;
import com.carrental.dto.response.AuthResponse;
import com.carrental.entity.AppUser;
import com.carrental.entity.RefreshToken;
import com.carrental.exception.BadRequestException;
import com.carrental.mapper.UserMapper;
import com.carrental.repository.RefreshTokenRepository;
import com.carrental.repository.UserRepository;
import com.carrental.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public AuthResponse register(RegisterRequest request) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new BadRequestException("Email already exists");
		}

		AppUser user = UserMapper.toEntity(request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		AppUser savedUser = userRepository.save(user);

		String accessToken = jwtService.generateToken(savedUser);
		String refreshTokenValue = jwtService.generateRefreshToken(savedUser);

		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(refreshTokenValue);
		refreshToken.setUser(savedUser);
		refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
		refreshToken.setRevoked(false);

		refreshTokenRepository.save(refreshToken);

		return new AuthResponse(
				accessToken,
				refreshTokenValue,
				UserMapper.toResponse(savedUser)
		);
	}

	public AuthResponse login(LoginRequest request) {

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getEmail(),
						request.getPassword()
				)
		);

		AppUser user = userRepository
				.findByEmail(request.getEmail())
				.orElseThrow(() -> new BadRequestException("User not found"));

		String accessToken = jwtService.generateToken(user);
		String refreshTokenValue = jwtService.generateRefreshToken(user);

		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(refreshTokenValue);
		refreshToken.setUser(user);
		refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
		refreshToken.setRevoked(false);

		refreshTokenRepository.save(refreshToken);

		return new AuthResponse(
				accessToken,
				refreshTokenValue,
				UserMapper.toResponse(user)
		);
	}

	public AuthResponse refreshToken(RefreshTokenRequest request) {

		RefreshToken refreshToken = refreshTokenRepository
				.findByToken(request.getRefreshToken())
				.orElseThrow(() -> new BadRequestException("Invalid refresh token"));

		if (refreshToken.isRevoked()) {
			throw new BadRequestException("Refresh token revoked");
		}

		if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			throw new BadRequestException("Refresh token expired");
		}

		AppUser user = refreshToken.getUser();
		String newAccessToken = jwtService.generateToken(user);

		return new AuthResponse(
				newAccessToken,
				refreshToken.getToken(),
				UserMapper.toResponse(user)
		);
	}

	public void logout(String refreshTokenValue) {
		RefreshToken refreshToken = refreshTokenRepository
				.findByToken(refreshTokenValue)
				.orElse(null);

		if (refreshToken != null) {
			refreshToken.setRevoked(true);
			refreshTokenRepository.save(refreshToken);
		}
	}
}