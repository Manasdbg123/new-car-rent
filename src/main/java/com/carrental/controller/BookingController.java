package com.carrental.controller;

import com.carrental.dto.request.BookingRequest;
import com.carrental.dto.response.ApiResponse;
import com.carrental.dto.response.BookingResponse;
import com.carrental.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

	private final BookingService bookingService;

	@PostMapping
	public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
			@Valid @RequestBody BookingRequest request,
			Authentication authentication
	) {

		BookingResponse response = bookingService.createBooking(
				getUserId(authentication),
				request
		);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(
						ApiResponse.<BookingResponse>builder()
								.success(true)
								.message("Booking created successfully")
								.data(response)
								.timestamp(LocalDateTime.now())
								.build()
				);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<BookingResponse>>> getUserBookings(
			Authentication authentication
	) {

		return ResponseEntity.ok(
				ApiResponse.<List<BookingResponse>>builder()
						.success(true)
						.message("Bookings fetched successfully")
						.data(
								bookingService.getUserBookings(
										getUserId(authentication)
								)
						)
						.timestamp(LocalDateTime.now())
						.build()
		);
	}

	@PutMapping("/{bookingId}/cancel")
	public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(
			@PathVariable Long bookingId,
			Authentication authentication
	) {

		return ResponseEntity.ok(
				ApiResponse.<BookingResponse>builder()
						.success(true)
						.message("Booking cancelled successfully")
						.data(
								bookingService.cancelBooking(
										bookingId,
										getUserId(authentication)
								)
						)
						.timestamp(LocalDateTime.now())
						.build()
		);
	}

	private Long getUserId(Authentication authentication) {

		return Long.parseLong(authentication.getName());
	}
}