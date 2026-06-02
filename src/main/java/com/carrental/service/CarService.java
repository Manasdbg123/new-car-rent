package com.carrental.service;

import com.carrental.dto.request.CarRequest;
import com.carrental.dto.response.CarResponse;
import com.carrental.entity.Car;
import com.carrental.entity.CarStatus;
import com.carrental.exception.BadRequestException;
import com.carrental.exception.ResourceNotFoundException;
import com.carrental.mapper.CarMapper;
import com.carrental.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarService {

	private final CarRepository carRepository;
	private final CarMapper carMapper;

	public List<CarResponse> getAllCars() {
		return carRepository.findAll().stream().map(carMapper::toResponse).toList();
	}

	public Page<CarResponse> getAvailableCars(
			String city, String brand, String vehicleType,
			String transmission, String fuelType,
			BigDecimal minPrice, BigDecimal maxPrice,
			int page, int size, String sortBy
	) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
		return carRepository.searchAvailableCars(
						CarStatus.AVAILABLE, city, brand, vehicleType,
						transmission, fuelType, minPrice, maxPrice, pageable)
				.map(carMapper::toResponse);
	}

	public CarResponse getCarById(Long id) {
		Car car = carRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Car not found"));
		return carMapper.toResponse(car);
	}

	// NEW: Real-time inventory locking logic
	@Transactional
	public boolean lockCar(Long carId, String userEmail) {
		Car car = carRepository.findById(carId).orElseThrow(() -> new ResourceNotFoundException("Car not found"));

		if (car.getStatus() != CarStatus.AVAILABLE) return false;

		LocalDateTime now = LocalDateTime.now();

		// If someone else holds an active lock, deny the request
		if (car.getLockedUntil() != null && car.getLockedUntil().isAfter(now)) {
			if (!userEmail.equals(car.getLockedBy())) {
				return false;
			}
		}

		// Grant a 10-minute hold
		car.setLockedUntil(now.plusMinutes(10));
		car.setLockedBy(userEmail);
		carRepository.save(car);
		return true;
	}

	@Transactional
	public CarResponse createCar(CarRequest request) {
		if (carRepository.existsByLicensePlate(request.getLicensePlate())) {
			throw new BadRequestException("License plate already exists");
		}
		Car car = carMapper.toEntity(request);
		Car savedCar = carRepository.save(car);
		log.info("Car created successfully: {}", savedCar.getId());
		return carMapper.toResponse(savedCar);
	}

	@Transactional
	public CarResponse updateCar(Long id, CarRequest request) {
		Car car = carRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Car not found"));
		carMapper.updateEntity(car, request);
		Car updatedCar = carRepository.save(car);
		log.info("Car updated successfully: {}", updatedCar.getId());
		return carMapper.toResponse(updatedCar);
	}

	@Transactional
	public void deleteCar(Long id) {
		Car car = carRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Car not found"));
		carRepository.delete(car);
		log.info("Car deleted successfully: {}", id);
	}
}