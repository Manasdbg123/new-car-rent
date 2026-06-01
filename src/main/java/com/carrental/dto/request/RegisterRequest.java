package com.carrental.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

	@NotBlank(message = "Full name is required")
	private String fullName;

	@Email(message = "Invalid email")
	@NotBlank(message = "Email is required")
	private String email;

	@NotBlank(message = "Phone is required")
	private String phone;

	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;

	public RegisterRequest() {
	}

	// ==========================================
	// EXPLICIT GETTERS & SETTERS (LOMBOK BYPASS)
	// ==========================================

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}