package com.carrental.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	USER_NOT_FOUND("USER_NOT_FOUND", "User not found"),
	CAR_NOT_FOUND("CAR_NOT_FOUND", "Car not found"),
	BOOKING_NOT_FOUND("BOOKING_NOT_FOUND", "Booking not found"),

	INVALID_CREDENTIALS("INVALID_CREDENTIALS", "Invalid email or password"),
	ACCESS_DENIED("ACCESS_DENIED", "Access denied"),
	INVALID_TOKEN("INVALID_TOKEN", "Invalid token"),
	TOKEN_EXPIRED("TOKEN_EXPIRED", "Token expired"),

	EMAIL_ALREADY_EXISTS("EMAIL_ALREADY_EXISTS", "Email already exists"),
	CAR_ALREADY_BOOKED("CAR_ALREADY_BOOKED", "Car already booked for selected dates"),
	INVALID_BOOKING_DATES("INVALID_BOOKING_DATES", "Invalid booking dates"),

	VALIDATION_FAILED("VALIDATION_FAILED", "Validation failed"),
	INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal server error");

	private final String code;
	private final String message;
}