package com.carrental.repository;

import com.carrental.entity.AppUser;
import com.carrental.entity.Booking;
import com.carrental.entity.BookingStatus;
import com.carrental.entity.Car;
import com.carrental.entity.CarStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
		AppUser user = userRepository.save(AppUser.builder()
				.fullName("Test User")
				.email("user@example.com")
				.phone("+10000000001")
				.passwordHash("hash")
				.role(Role.ROLE_USER)
				.enabled(true)
				.build());

		Car car = carRepository.save(Car.builder()
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
				.build());

		LocalDateTime start = LocalDateTime.now().plusDays(2).withNano(0);
		LocalDateTime end = start.plusDays(3);
		bookingRepository.save(Booking.builder()
				.user(user)
				.car(car)
				.startAt(start)
				.endAt(end)
				.totalAmount(BigDecimal.valueOf(225))
				.status(BookingStatus.CONFIRMED)
				.build());

		assertThat(bookingRepository.existsOverlap(car.getId(), start.plusHours(1), end.plusHours(1), List.of(BookingStatus.CONFIRMED))).isTrue();
		assertThat(bookingRepository.existsOverlap(car.getId(), end, end.plusDays(1), List.of(BookingStatus.CONFIRMED))).isFalse();
		assertThat(bookingRepository.existsOverlap(car.getId(), start.plusHours(1), end.plusHours(1), List.of(BookingStatus.CANCELLED))).isFalse();
	}
}
