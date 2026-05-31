package com.carrental.controller;

import com.carrental.dto.request.CarRequest;
import com.carrental.dto.response.ApiResponse;
import com.carrental.dto.response.CarResponse;
import com.carrental.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

	private final CarService carService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<CarResponse>>> getAllCars() {

		return ResponseEntity.ok(
				ApiResponse.<List<CarResponse>>builder()
						.success(true)
						.message("Cars fetched successfully")
						.data(carService.getAllCars())
						.timestamp(LocalDateTime.now())
						.build()
		);
	}

	@GetMapping("/available")
	public ResponseEntity<ApiResponse<Page<CarResponse>>> getAvailableCars(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "createdAt") String sortBy
	) {

		return ResponseEntity.ok(
				ApiResponse.<Page<CarResponse>>builder()
						.success(true)
						.message("Available cars fetched successfully")
						.data(carService.getAvailableCars(page, size, sortBy))
						.timestamp(LocalDateTime.now())
						.build()
		);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<CarResponse>> getCarById(
			@PathVariable Long id
	) {

		return ResponseEntity.ok(
				ApiResponse.<CarResponse>builder()
						.success(true)
						.message("Car fetched successfully")
						.data(carService.getCarById(id))
						.timestamp(LocalDateTime.now())
						.build()
		);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<CarResponse>> createCar(
			@Valid @RequestBody CarRequest request
	) {

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(
						ApiResponse.<CarResponse>builder()
								.success(true)
								.message("Car created successfully")
								.data(carService.createCar(request))
								.timestamp(LocalDateTime.now())
								.build()
				);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<CarResponse>> updateCar(
			@PathVariable Long id,
			@Valid @RequestBody CarRequest request
	) {

		return ResponseEntity.ok(
				ApiResponse.<CarResponse>builder()
						.success(true)
						.message("Car updated successfully")
						.data(carService.updateCar(id, request))
						.timestamp(LocalDateTime.now())
						.build()
		);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteCar(
			@PathVariable Long id
	) {

		carService.deleteCar(id);

		return ResponseEntity.ok(
				ApiResponse.<Void>builder()
						.success(true)
						.message("Car deleted successfully")
						.timestamp(LocalDateTime.now())
						.build()
		);
	}
}