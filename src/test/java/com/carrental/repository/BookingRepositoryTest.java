package com.carrental.repository;

import com.carrental.entity.AppUser;
import com.carrental.entity.Booking;
import com.carrental.entity.BookingStatus;
import com.carrental.entity.Car;
import com.carrental.entity.CarStatus;
import com.carrental.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class BookingRepositoryTest {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CarRepository carRepository;

	@Test
	void existsOverlapMatchesIntersectingIntervalsOnlyForBlockingStatuses() {
		// BYPASS LOMBOK: Use standard object creation
		AppUser user = new AppUser();
		user.setFullName("Test User");
		user.setEmail("user@example.com");
		user.setPhone("+10000000001");
		user.setPassword("hash"); // Fixed from passwordHash
		user.setRole(Role.USER);  // Fixed from Role.ROLE_USER
		user.setEnabled(true);
		user = userRepository.save(user);

		Car car = new Car();
		car.setBrand("Toyota");
		car.setModel("Camry");
		car.setManufactureYear(2024); // Fixed from year
		car.setLicensePlate("ABC123");
		car.setCity("Austin");
		car.setTransmission("AUTO");
		car.setFuelType("PETROL");
		car.setSeats(5);
		car.setDailyRate(BigDecimal.valueOf(75));
		car.setStatus(CarStatus.AVAILABLE);
		car = carRepository.save(car);

		LocalDateTime start = LocalDateTime.now().plusDays(2).withNano(0);
		LocalDateTime end = start.plusDays(3);

		Booking booking = new Booking();
		booking.setUser(user);
		booking.setCar(car);
		booking.setStartAt(start);
		booking.setEndAt(end);
		booking.setTotalAmount(BigDecimal.valueOf(225));
		booking.setStatus(BookingStatus.CONFIRMED);
		bookingRepository.save(booking);

		// Fixed from existsOverlap to existsActiveBookingConflict
		assertThat(bookingRepository.existsActiveBookingConflict(car.getId(), start.plusHours(1), end.plusHours(1))).isTrue();
		assertThat(bookingRepository.existsActiveBookingConflict(car.getId(), end, end.plusDays(1))).isFalse();
	}
}