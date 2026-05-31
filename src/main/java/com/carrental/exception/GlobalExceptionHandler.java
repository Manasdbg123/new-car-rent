package com.carrental.exception;

import com.carrental.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(
			ResourceNotFoundException ex,
			HttpServletRequest request
	) {

		ErrorResponse response = ErrorResponse.builder()
				.status(HttpStatus.NOT_FOUND.value())
				.error("Not Found")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.timestamp(LocalDateTime.now())
				.build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleBadRequest(
			BadRequestException ex,
			HttpServletRequest request
	) {

		ErrorResponse response = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.error("Bad Request")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.timestamp(LocalDateTime.now())
				.build();

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ErrorResponse> handleConflict(
			ConflictException ex,
			HttpServletRequest request
	) {

		ErrorResponse response = ErrorResponse.builder()
				.status(HttpStatus.CONFLICT.value())
				.error("Conflict")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.timestamp(LocalDateTime.now())
				.build();

		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorResponse> handleUnauthorized(
			UnauthorizedException ex,
			HttpServletRequest request
	) {

		ErrorResponse response = ErrorResponse.builder()
				.status(HttpStatus.UNAUTHORIZED.value())
				.error("Unauthorized")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.timestamp(LocalDateTime.now())
				.build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(
			MethodArgumentNotValidException ex,
			HttpServletRequest request
	) {

		Map<String, String> errors = new HashMap<>();

		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}

		ErrorResponse response = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.error("Validation Failed")
				.message("Input validation failed")
				.validationErrors(errors)
				.path(request.getRequestURI())
				.timestamp(LocalDateTime.now())
				.build();

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolation(
			ConstraintViolationException ex,
			HttpServletRequest request
	) {

		ErrorResponse response = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.error("Constraint Violation")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.timestamp(LocalDateTime.now())
				.build();

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDenied(
			AccessDeniedException ex,
			HttpServletRequest request
	) {

		ErrorResponse response = ErrorResponse.builder()
				.status(HttpStatus.FORBIDDEN.value())
				.error("Forbidden")
				.message("You do not have permission to access this resource")
				.path(request.getRequestURI())
				.timestamp(LocalDateTime.now())
				.build();

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneral(
			Exception ex,
			HttpServletRequest request
	) {

		ErrorResponse response = ErrorResponse.builder()
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.error("Internal Server Error")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.timestamp(LocalDateTime.now())
				.build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(response);
	}
}