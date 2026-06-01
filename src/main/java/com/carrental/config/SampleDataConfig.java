package com.carrental.config;

import com.carrental.entity.Car;
import com.carrental.entity.CarStatus;
import com.carrental.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
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

		List<Car> cars = new ArrayList<>();

		// Car 1
		Car car1 = new Car();
		car1.setBrand("BMW");
		car1.setModel("M4");
		car1.setManufactureYear(2024);
		car1.setLicensePlate("BMW-M4-001");
		car1.setDailyRate(BigDecimal.valueOf(120));
		car1.setSeats(4);
		car1.setFuelType("Petrol");
		car1.setTransmission("Automatic");
		car1.setCity("Bangalore");
		car1.setImageUrl("/images/bmw.jpg");
		car1.setDescription("Luxury sports performance car");
		car1.setStatus(CarStatus.AVAILABLE);
		cars.add(car1);

		// Car 2
		Car car2 = new Car();
		car2.setBrand("Audi");
		car2.setModel("A6");
		car2.setManufactureYear(2023);
		car2.setLicensePlate("AUDI-A6-002");
		car2.setDailyRate(BigDecimal.valueOf(95));
		car2.setSeats(5);
		car2.setFuelType("Diesel");
		car2.setTransmission("Automatic");
		car2.setCity("Hyderabad");
		car2.setImageUrl("/images/audi.jpg");
		car2.setDescription("Premium executive sedan");
		car2.setStatus(CarStatus.AVAILABLE);
		cars.add(car2);

		// Car 3
		Car car3 = new Car();
		car3.setBrand("Mercedes");
		car3.setModel("C-Class");
		car3.setManufactureYear(2024);
		car3.setLicensePlate("MERC-C-003");
		car3.setDailyRate(BigDecimal.valueOf(110));
		car3.setSeats(5);
		car3.setFuelType("Petrol");
		car3.setTransmission("Automatic");
		car3.setCity("Delhi");
		car3.setImageUrl("/images/mercedes.jpg");
		car3.setDescription("Comfort and elegance combined");
		car3.setStatus(CarStatus.AVAILABLE);
		cars.add(car3);

		carRepository.saveAll(cars);

		log.info("Sample car data inserted successfully");
	}
}