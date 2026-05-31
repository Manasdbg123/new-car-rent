package com.carrental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(
			nullable = false,
			length = 512
	)
	private String token;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "user_id",
			nullable = false
	)
	private AppUser user;

	@Column(nullable = false)
	private LocalDateTime expiryDate;

	@Column(nullable = false)
	private boolean revoked;
}