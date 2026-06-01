package com.carrental.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 512)
	private String token;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private AppUser user;

	@Column(nullable = false)
	private LocalDateTime expiryDate;

	@Column(nullable = false)
	private boolean revoked;

	public RefreshToken() {}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getToken() { return token; }
	public void setToken(String token) { this.token = token; }
	public AppUser getUser() { return user; }
	public void setUser(AppUser user) { this.user = user; }
	public LocalDateTime getExpiryDate() { return expiryDate; }
	public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }
	public boolean isRevoked() { return revoked; }
	public void setRevoked(boolean revoked) { this.revoked = revoked; }
}