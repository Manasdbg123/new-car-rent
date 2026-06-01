package com.carrental.security;

import com.carrental.entity.AppUser;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserPrincipal extends User {

	private final Long userId;

	public UserPrincipal(AppUser user) {

		super(
				user.getEmail(),
				user.getPassword(),
				List.of(
						new SimpleGrantedAuthority(
								"ROLE_" + user.getRole().name()
						)
				)
		);

		this.userId = user.getId();
	}
}