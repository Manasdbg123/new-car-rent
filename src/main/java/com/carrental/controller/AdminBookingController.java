package com.carrental.controller;

import com.carrental.dto.response.ApiResponse;
import com.carrental.dto.response.BookingResponse;
import com.carrental.entity.Booking;
import com.carrental.mapper.BookingMapper;
import com.carrental.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/bookings")
@RequiredArgsConstructor
public class AdminBookingController {

	private final BookingRepository bookingRepository;
	private final BookingMapper bookingMapper;

	// =========================================
	// GET ALL BOOKINGS
	// =========================================

	@GetMapping
	public ResponseEntity<ApiResponse<List<BookingResponse>>>
	getAllBookings() {

		List<BookingResponse> bookings =

				bookingRepository.findAll()
						.stream()
						.map(bookingMapper::toResponse)
						.toList();

		return ResponseEntity.ok(

				ApiResponse.<List<BookingResponse>>builder()
						.success(true)
						.message("Bookings fetched successfully")
						.data(bookings)
						.timestamp(LocalDateTime.now())
						.build()
		);
	}
}