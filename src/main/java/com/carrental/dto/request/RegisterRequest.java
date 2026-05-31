package com.carrental.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
}