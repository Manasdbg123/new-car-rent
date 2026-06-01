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

		// Bypassing Lombok Builder and using explicit setters
		AppUser admin = new AppUser();
		admin.setFullName("System Admin");
		admin.setEmail(adminEmail);
		admin.setPhone("9999999999");
		admin.setPassword(passwordEncoder.encode("Admin@123"));
		admin.setRole(Role.ADMIN);
		admin.setEnabled(true);
		admin.setAccountNonLocked(true);

		userRepository.save(admin);

		log.info("Default admin user created successfully");
	}
}