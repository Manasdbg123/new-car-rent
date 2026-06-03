package com.carrental.controller;

import com.carrental.dto.request.BookingRequest;
import com.carrental.dto.response.ApiResponse;
import com.carrental.dto.response.BookingResponse;
import com.carrental.entity.AppUser;
import com.carrental.repository.UserRepository;
import com.carrental.service.BookingService;
import com.carrental.service.EmailService; // <-- NEW IMPORT

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

	private final BookingService bookingService;
	private final UserRepository userRepository;
	private final EmailService emailService; // <-- NEW FIELD

	// Explicit constructor to bypass Lombok issues
	public BookingController(BookingService bookingService, UserRepository userRepository, EmailService emailService) {
		this.bookingService = bookingService;
		this.userRepository = userRepository;
		this.emailService = emailService; // <-- INJECTED
	}

	@PostMapping
	public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
			// Removed strict @Valid to prevent immediate 400 errors if time is too close to "Now"
			@RequestBody BookingRequest request,
			Principal principal
	) {
		// Bulletproof identity extraction
		AppUser user = userRepository.findByEmail(principal.getName())
				.orElseThrow(() -> new RuntimeException("User identity error"));

		BookingResponse response = bookingService.createBooking(user.getId(), request);

		// ==========================================
		// NEW: TRIGGER BACKGROUND INVOICE EMAIL
		// ==========================================
		// Grabbing details from the response DTO to format the email
		String carName = response.getCarBrand() + " " + response.getCarModel();
		String totalAmount = String.valueOf(response.getTotalAmount());

		emailService.sendBookingInvoice(
				user.getEmail(),
				user.getFullName(),
				carName,
				totalAmount
		);
		// ==========================================

		return ResponseEntity.status(HttpStatus.CREATED).body(
				ApiResponse.<BookingResponse>builder()
						.success(true)
						.message("Booking successful")
						.data(response)
						.build()
		);
	}

	@GetMapping("/my-bookings")
	public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookings(Principal principal) {
		// Bulletproof identity extraction
		AppUser user = userRepository.findByEmail(principal.getName())
				.orElseThrow(() -> new RuntimeException("User identity error"));

		List<BookingResponse> responses = bookingService.getUserBookings(user.getId());

		return ResponseEntity.ok(
				ApiResponse.<List<BookingResponse>>builder()
						.success(true)
						.message("Fetched bookings")
						.data(responses)
						.build()
		);
	}

	@PostMapping("/{id}/cancel")
	public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(
			@PathVariable Long id,
			Principal principal
	) {
		// Bulletproof identity extraction
		AppUser user = userRepository.findByEmail(principal.getName())
				.orElseThrow(() -> new RuntimeException("User identity error"));

		BookingResponse response = bookingService.cancelBooking(id, user.getId());

		return ResponseEntity.ok(
				ApiResponse.<BookingResponse>builder()
						.success(true)
						.message("Cancelled successfully")
						.data(response)
						.build()
		);
	}
}