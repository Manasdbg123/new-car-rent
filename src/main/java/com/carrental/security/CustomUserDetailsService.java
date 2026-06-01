package com.carrental.security;

import com.carrental.entity.AppUser;
import com.carrental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email)
			throws UsernameNotFoundException {

		AppUser user = userRepository
				.findByEmail(email)
				.orElseThrow(() ->
						new UsernameNotFoundException(
								"User not found"
						)
				);

		return new User(
				user.getEmail(),
				user.getPassword(),
				List.of(
						new SimpleGrantedAuthority(
								"ROLE_" + user.getRole().name()
						)
				)
		);
	}
}