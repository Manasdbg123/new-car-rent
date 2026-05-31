package com.carrental.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String fullName;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String phone;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Builder.Default
	@Column(nullable = false)
	private boolean enabled = true;

	@Builder.Default
	@Column(nullable = false)
	private boolean accountNonLocked = true;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@PrePersist
	public void prePersist() {

		this.createdAt = LocalDateTime.now();

		if (this.role == null) {
			this.role = Role.USER;
		}
	}
}