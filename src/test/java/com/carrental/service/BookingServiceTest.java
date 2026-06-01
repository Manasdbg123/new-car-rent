package com.carrental.service;

import com.carrental.dto.request.BookingRequest;
import com.carrental.entity.AppUser;
import com.carrental.entity.Car;
import com.carrental.entity.CarStatus;
import com.carrental.entity.Role;
import com.carrental.exception.BadRequestException;
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
		// Fixed: carRepository.findById, NOT findByIdForUpdate
		when(carRepository.findById(10L)).thenReturn(Optional.of(car()));

		// Fixed: existsActiveBookingConflict
		when(bookingRepository.existsActiveBookingConflict(10L, start, end)).thenReturn(true);

		// Fixed: bookingService.createBooking
		assertThatThrownBy(() -> bookingService.createBooking(1L, request))
				.isInstanceOf(BadRequestException.class) // Changed to BadRequestException based on our stabilized service
				.hasMessageContaining("Car already booked");
	}

	private AppUser user() {
		AppUser u = new AppUser();
		u.setId(1L);
		u.setFullName("Test User");
		u.setEmail("user@example.com");
		u.setPhone("+10000000001");
		u.setPassword("hash");
		u.setRole(Role.USER);
		u.setEnabled(true);
		return u;
	}

	private Car car() {
		Car c = new Car();
		c.setId(10L);
		c.setBrand("Toyota");
		c.setModel("Camry");
		c.setManufactureYear(2024);
		c.setLicensePlate("ABC123");
		c.setCity("Austin");
		c.setTransmission("AUTO");
		c.setFuelType("PETROL");
		c.setSeats(5);
		c.setDailyRate(BigDecimal.valueOf(75));
		c.setStatus(CarStatus.AVAILABLE);
		return c;
	}
}