package com.carrental.mapper;

import com.carrental.dto.request.CarRequest;
import com.carrental.dto.response.CarResponse;
import com.carrental.entity.Car;
import com.carrental.entity.CarStatus;
import org.springframework.stereotype.Component;

@Component
public class CarMapper {

	public CarResponse toResponse(Car car) {

		if (car == null) {
			return null;
		}

		return CarResponse.builder()
				.id(car.getId())
				.brand(car.getBrand())
				.model(car.getModel())
				.manufactureYear(car.getManufactureYear())
				.licensePlate(car.getLicensePlate())
				.dailyRate(car.getDailyRate())
				.seats(car.getSeats())
				.fuelType(car.getFuelType())
				.transmission(car.getTransmission())
				.city(car.getCity())
				.imageUrl(car.getImageUrl())
				.description(car.getDescription())
				.status(car.getStatus() != null ? car.getStatus().name() : null)
				.createdAt(car.getCreatedAt())
				.build();
	}

	public Car toEntity(CarRequest request) {

		if (request == null) {
			return null;
		}

		Car car = new Car();
		car.setBrand(request.getBrand());
		car.setModel(request.getModel());
		car.setManufactureYear(request.getManufactureYear());
		car.setLicensePlate(request.getLicensePlate());
		car.setDailyRate(request.getDailyRate());
		car.setSeats(request.getSeats());
		car.setFuelType(request.getFuelType());
		car.setTransmission(request.getTransmission());
		car.setCity(request.getCity());
		car.setImageUrl(request.getImageUrl());
		car.setDescription(request.getDescription());

		if (request.getStatus() != null) {
			car.setStatus(CarStatus.valueOf(request.getStatus().toUpperCase()));
		} else {
			car.setStatus(CarStatus.AVAILABLE);
		}

		return car;
	}

	public void updateEntity(Car car, CarRequest request) {

		car.setBrand(request.getBrand());
		car.setModel(request.getModel());
		car.setManufactureYear(request.getManufactureYear());
		car.setLicensePlate(request.getLicensePlate());
		car.setDailyRate(request.getDailyRate());
		car.setSeats(request.getSeats());
		car.setFuelType(request.getFuelType());
		car.setTransmission(request.getTransmission());
		car.setCity(request.getCity());
		car.setImageUrl(request.getImageUrl());
		car.setDescription(request.getDescription());

		if (request.getStatus() != null) {
			car.setStatus(CarStatus.valueOf(request.getStatus().toUpperCase()));
		}
	}
}