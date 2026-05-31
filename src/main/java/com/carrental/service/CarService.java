package com.carrental.service;

import com.carrental.dto.request.CarRequest;
import com.carrental.dto.response.CarResponse;
import com.carrental.entity.Car;
import com.carrental.exception.BadRequestException;
import com.carrental.exception.ResourceNotFoundException;
import com.carrental.mapper.CarMapper;
import com.carrental.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarService {

	private final CarRepository carRepository;
	private final CarMapper carMapper;

	public List<CarResponse> getAllCars() {

		return carRepository.findAll()
				.stream()
				.map(carMapper::toResponse)
				.toList();
	}

	public Page<CarResponse> getAvailableCars(
			int page,
			int size,
			String sortBy
	) {

		Pageable pageable = PageRequest.of(
				page,
				size,
				Sort.by(sortBy).descending()
		);

		return carRepository.findByAvailableTrue(pageable)
				.map(carMapper::toResponse);
	}

	public CarResponse getCarById(Long id) {

		Car car = carRepository.findById(id)
				.orElseThrow(() ->
						new ResourceNotFoundException("Car not found")
				);

		return carMapper.toResponse(car);
	}

	@Transactional
	public CarResponse createCar(CarRequest request) {

		if (carRepository.existsByRegistrationNumber(
				request.getRegistrationNumber()
		)) {
			throw new BadRequestException(
					"Registration number already exists"
			);
		}

		Car car = carMapper.toEntity(request);

		Car savedCar = carRepository.save(car);

		log.info("Car created successfully: {}", savedCar.getId());

		return carMapper.toResponse(savedCar);
	}

	@Transactional
	public CarResponse updateCar(Long id, CarRequest request) {

		Car car = carRepository.findById(id)
				.orElseThrow(() ->
						new ResourceNotFoundException("Car not found")
				);

		carMapper.updateEntity(car, request);

		Car updatedCar = carRepository.save(car);

		log.info("Car updated successfully: {}", updatedCar.getId());

		return carMapper.toResponse(updatedCar);
	}

	@Transactional
	public void deleteCar(Long id) {

		Car car = carRepository.findById(id)
				.orElseThrow(() ->
						new ResourceNotFoundException("Car not found")
				);

		carRepository.delete(car);

		log.info("Car deleted successfully: {}", id);
	}
}