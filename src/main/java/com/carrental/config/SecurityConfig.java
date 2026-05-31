package com.carrental.config;

import com.carrental.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration configuration
	) throws Exception {

		return configuration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(
			HttpSecurity http
	) throws Exception {

		http

				.csrf(csrf -> csrf.disable())

				.sessionManagement(session ->
						session.sessionCreationPolicy(
								SessionCreationPolicy.STATELESS
						)
				)

				.authorizeHttpRequests(auth -> auth

						/*
                        =====================================
                        PUBLIC FRONTEND
                        =====================================
                        */

						.requestMatchers(
								"/",
								"/index.html",

								"/css/**",
								"/js/**",
								"/images/**",

								"/favicon.ico",

								"/error",

								"/api/auth/**",

								"/swagger-ui/**",
								"/v3/api-docs/**"

						).permitAll()

						/*
                        =====================================
                        ADMIN
                        =====================================
                        */

						.requestMatchers(
								"/api/admin/**"
						).hasRole("ADMIN")

						/*
                        =====================================
                        ALL OTHERS
                        =====================================
                        */

						.anyRequest().permitAll()
				)

				.addFilterBefore(
						jwtAuthenticationFilter,
						UsernamePasswordAuthenticationFilter.class
				);

		return http.build();
	}
}