package com.carrental.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

	private SecurityUtils() {
	}

	public static String getCurrentUserEmail() {

		Authentication authentication =
				SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			return null;
		}

		return authentication.getName();
	}

	public static boolean hasRole(String role) {

		Authentication authentication =
				SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			return false;
		}

		return authentication.getAuthorities()
				.stream()
				.anyMatch(a -> a.getAuthority().equals(role));
	}
}