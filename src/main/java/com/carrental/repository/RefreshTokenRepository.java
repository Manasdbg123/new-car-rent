package com.carrental.repository;

import com.carrental.entity.AppUser;
import com.carrental.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByToken(String token);

	Optional<RefreshToken> findByUser(AppUser user);

	void deleteByUser(AppUser user);
}