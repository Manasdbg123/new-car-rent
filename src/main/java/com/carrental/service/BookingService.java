package com.carrental.service;

import com.carrental.dto.request.BookingRequest;
import com.carrental.dto.response.BookingResponse;
import com.carrental.entity.*;
import com.carrental.exception.BadRequestException;
import com.carrental.exception.ResourceNotFoundException;
import com.carrental.mapper.BookingMapper;
import com.carrental.repository.BookingRepository;
import com.carrental.repository.CarRepository;
import com.carrental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

	private final BookingRepository bookingRepository;
	private final CarRepository carRepository;
	private final UserRepository userRepository;
	private final BookingMapper bookingMapper;

	@Transactional
	public BookingResponse createBooking(
			Long userId,
			BookingRequest request
	) {
		// FIXED: Using getEndAt() and getStartAt() to match updated BookingRequest DTO
		if (request.getEndAt().isBefore(request.getStartAt())) {
			throw new BadRequestException("End date must be after start date");
		}

		AppUser user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		Car car = carRepository.findById(request.getCarId())
				.orElseThrow(() -> new ResourceNotFoundException("Car not found"));

		// FIXED: Passing correct updated getter fields
		boolean booked = bookingRepository.existsActiveBookingConflict(
				car.getId(),
				request.getStartAt(),
				request.getEndAt()
		);

		if (booked) {
			throw new BadRequestException("Car already booked for selected dates");
		}

		long days = ChronoUnit.DAYS.between(
				request.getStartAt(),
				request.getEndAt()
		);

		if (days <= 0) {
			throw new BadRequestException("Booking duration must be at least 1 day");
		}

		// FIXED: Car entity uses getDailyRate() mapped from daily_rate column
		BigDecimal totalAmount = car.getDailyRate().multiply(BigDecimal.valueOf(days));

		// FIXED: Booking entity builder uses startAt, endAt, and totalAmount
		Booking booking = Booking.builder()
				.user(user)
				.car(car)
				.startAt(request.getStartAt())
				.endAt(request.getEndAt())
				.totalAmount(totalAmount)
				.status(BookingStatus.CONFIRMED)
				.build();

		Booking savedBooking = bookingRepository.save(booking);

		log.info("Booking created successfully: {}", savedBooking.getId());

		return bookingMapper.toResponse(savedBooking);
	}

	@Transactional(readOnly = true)
	public List<BookingResponse> getUserBookings(Long userId) {
		return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId)
				.stream()
				.map(bookingMapper::toResponse)
				.toList();
	}

	@Transactional
	public BookingResponse cancelBooking(
			Long bookingId,
			Long userId
	) {
		Booking booking = bookingRepository
				.findByIdAndUserId(bookingId, userId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

		booking.setStatus(BookingStatus.CANCELLED);
		Booking updatedBooking = bookingRepository.save(booking);

		log.info("Booking cancelled successfully: {}", bookingId);

		return bookingMapper.toResponse(updatedBooking);
	}
}