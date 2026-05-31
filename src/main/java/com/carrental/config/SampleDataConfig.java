package com.carrental.config;

import com.carrental.entity.Car;
import com.carrental.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SampleDataConfig implements CommandLineRunner {

	private final CarRepository carRepository;

	@Override
	public void run(String... args) {

		if (carRepository.count() > 0) {
			return;
		}

		List<Car> cars = List.of(

				Car.builder()
						.brand("BMW")
						.model("M4")
						.type("Sports")
						.year(2024)
						.registrationNumber("BMW-M4-001")
						.pricePerDay(BigDecimal.valueOf(120))
						.seatingCapacity(4)
						.fuelType("Petrol")
						.transmission("Automatic")
						.city("Bangalore")
						.imageUrl("/images/bmw.jpg")
						.description("Luxury sports performance car")
						.available(true)
						.build(),

				Car.builder()
						.brand("Audi")
						.model("A6")
						.type("Sedan")
						.year(2023)
						.registrationNumber("AUDI-A6-002")
						.pricePerDay(BigDecimal.valueOf(95))
						.seatingCapacity(5)
						.fuelType("Diesel")
						.transmission("Automatic")
						.city("Hyderabad")
						.imageUrl("/images/audi.jpg")
						.description("Premium executive sedan")
						.available(true)
						.build(),

				Car.builder()
						.brand("Mercedes")
						.model("C-Class")
						.type("Luxury")
						.year(2024)
						.registrationNumber("MERC-C-003")
						.pricePerDay(BigDecimal.valueOf(110))
						.seatingCapacity(5)
						.fuelType("Petrol")
						.transmission("Automatic")
						.city("Delhi")
						.imageUrl("/images/mercedes.jpg")
						.description("Comfort and elegance combined")
						.available(true)
						.build()
		);

		carRepository.saveAll(cars);

		log.info("Sample car data inserted successfully");
	}
}