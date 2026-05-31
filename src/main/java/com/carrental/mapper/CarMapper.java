package com.carrental.mapper;

import com.carrental.dto.request.CarRequest;
import com.carrental.dto.response.CarResponse;
import com.carrental.entity.Car;
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
				.type(car.getType())
				.year(car.getYear())
				.registrationNumber(car.getRegistrationNumber())
				.pricePerDay(car.getPricePerDay())
				.seatingCapacity(car.getSeatingCapacity())
				.fuelType(car.getFuelType())
				.transmission(car.getTransmission())
				.city(car.getCity())
				.imageUrl(car.getImageUrl())
				.description(car.getDescription())
				.available(car.getAvailable())
				.createdAt(car.getCreatedAt())
				.build();
	}

	public Car toEntity(CarRequest request) {

		if (request == null) {
			return null;
		}

		return Car.builder()
				.brand(request.getBrand())
				.model(request.getModel())
				.type(request.getType())
				.year(request.getYear())
				.registrationNumber(request.getRegistrationNumber())
				.pricePerDay(request.getPricePerDay())
				.seatingCapacity(request.getSeatingCapacity())
				.fuelType(request.getFuelType())
				.transmission(request.getTransmission())
				.city(request.getCity())
				.imageUrl(request.getImageUrl())
				.description(request.getDescription())
				.available(
						request.getAvailable() != null
								? request.getAvailable()
								: true
				)
				.build();
	}

	public void updateEntity(Car car, CarRequest request) {

		car.setBrand(request.getBrand());
		car.setModel(request.getModel());
		car.setType(request.getType());
		car.setYear(request.getYear());
		car.setRegistrationNumber(request.getRegistrationNumber());
		car.setPricePerDay(request.getPricePerDay());
		car.setSeatingCapacity(request.getSeatingCapacity());
		car.setFuelType(request.getFuelType());
		car.setTransmission(request.getTransmission());
		car.setCity(request.getCity());
		car.setImageUrl(request.getImageUrl());
		car.setDescription(request.getDescription());

		if (request.getAvailable() != null) {
			car.setAvailable(request.getAvailable());
		}
	}
}