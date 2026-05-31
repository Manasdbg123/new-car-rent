package com.carrental.config;

import com.carrental.entity.AppUser;
import com.carrental.entity.Role;
import com.carrental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminBootstrapConfig implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) {

		String adminEmail = "admin@carrental.com";

		if (userRepository.existsByEmail(adminEmail)) {
			return;
		}

		AppUser admin = AppUser.builder()
				.fullName("System Admin")
				.email(adminEmail)
				.phone("9999999999")
				.password(passwordEncoder.encode("Admin@123"))
				.role(Role.ADMIN)
				.enabled(true)
				.accountNonLocked(true)
				.build();

		userRepository.save(admin);

		log.info("Default admin user created successfully");
	}
}