package com.carrental.mapper;

import com.carrental.dto.request.RegisterRequest;
import com.carrental.dto.response.UserResponse;
import com.carrental.entity.AppUser;
import com.carrental.entity.Role;

public class UserMapper {

	private UserMapper() {
	}

	public static AppUser toEntity(RegisterRequest request) {
		AppUser user = new AppUser();
		user.setFullName(request.getFullName());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());
		user.setRole(Role.USER);
		return user;
	}

	public static UserResponse toResponse(AppUser user) {
		return new UserResponse(
				user.getId(),
				user.getFullName(),
				user.getEmail(),
				user.getPhone(),
				user.getRole().name()
		);
	}
}