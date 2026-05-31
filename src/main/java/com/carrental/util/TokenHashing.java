package com.carrental.util;

import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@UtilityClass
public class TokenHashing {

	public String hashToken(String token) {

		try {

			MessageDigest digest =
					MessageDigest.getInstance("SHA-256");

			byte[] encodedHash =
					digest.digest(
							token.getBytes(StandardCharsets.UTF_8)
					);

			StringBuilder hexString = new StringBuilder();

			for (byte b : encodedHash) {
				hexString.append(
						String.format("%02x", b)
				);
			}

			return hexString.toString();

		} catch (NoSuchAlgorithmException ex) {

			throw new RuntimeException(
					"Error hashing token",
					ex
			);
		}
	}
}