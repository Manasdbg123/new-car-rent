package com.carrental.dto.response;

public record UserResponse(

		Long id,

		String fullName,

		String email,

		String phone,

		String role

) {
}