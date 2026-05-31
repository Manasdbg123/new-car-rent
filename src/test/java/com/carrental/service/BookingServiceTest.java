package com.carrental.service;

import com.carrental.dto.request.BookingRequest;
import com.carrental.entity.AppUser;
import com.carrental.entity.Car;
import com.carrental.entity.CarStatus;
import com.carrental.exception.ApiException;
import com.carrental.mapper.BookingMapper;
import com.carrental.repository.BookingRepository;
import com.carrental.repository.CarRepository;
import com.carrental.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

	@Mock
	private BookingRepository bookingRepository;

	@Mock
	private CarRepository carRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private BookingMapper bookingMapper;

	@InjectMocks
	private BookingService bookingService;

	@Test
	void bookRejectsOverlappingConfirmedBooking() {
		LocalDateTime start = LocalDateTime.now().plusDays(1);
		LocalDateTime end = start.plusDays(2);
		BookingRequest request = new BookingRequest(10L, start, end);

		when(userRepository.findById(1L)).thenReturn(Optional.of(user()));
		when(carRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(car()));
		when(bookingRepository.existsOverlap(eq(10L), eq(start), eq(end), any())).thenReturn(true);

		assertThatThrownBy(() -> bookingService.book(1L, request))
				.isInstanceOf(ApiException.class)
				.hasMessage("Car already booked for the selected interval");
	}

	private AppUser user() {
		return AppUser.builder()
				.id(1L)
				.fullName("Test User")
				.email("user@example.com")
				.phone("+10000000001")
				.passwordHash("hash")
				.role(Role.ROLE_USER)
				.enabled(true)
				.build();
	}

	private Car car() {
		return Car.builder()
				.id(10L)
				.brand("Toyota")
				.model("Camry")
				.year(2024)
				.licensePlate("ABC123")
				.city("Austin")
				.transmission("AUTO")
				.fuelType("PETROL")
				.seats(5)
				.dailyRate(BigDecimal.valueOf(75))
				.status(CarStatus.AVAILABLE)
				.build();
	}
}
