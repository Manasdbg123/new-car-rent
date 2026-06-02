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

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

	private final CarService carService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<CarResponse>>> getAllCars() {
		return ResponseEntity.ok(ApiResponse.<List<CarResponse>>builder()
				.success(true)
				.data(carService.getAllCars())
				.timestamp(LocalDateTime.now())
				.build());
	}

	@GetMapping("/available")
	public ResponseEntity<ApiResponse<Page<CarResponse>>> getAvailableCars(
			@RequestParam(required = false) String city,
			@RequestParam(required = false) String brand,
			@RequestParam(required = false) String vehicleType,
			@RequestParam(required = false) String transmission,
			@RequestParam(required = false) String fuelType,
			@RequestParam(required = false) BigDecimal minPrice,
			@RequestParam(required = false) BigDecimal maxPrice,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "createdAt") String sortBy
	) {
		return ResponseEntity.ok(ApiResponse.<Page<CarResponse>>builder()
				.success(true)
				.data(carService.getAvailableCars(city, brand, vehicleType, transmission, fuelType, minPrice, maxPrice, page, size, sortBy))
				.timestamp(LocalDateTime.now())
				.build());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<CarResponse>> getCarById(@PathVariable Long id) {
		// FIXED: Removed the extra '>' after CarResponse
		return ResponseEntity.ok(ApiResponse.<CarResponse>builder()
				.success(true)
				.data(carService.getCarById(id))
				.timestamp(LocalDateTime.now())
				.build());
	}

	@PostMapping("/{id}/lock")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponse<Boolean>> lockCar(@PathVariable Long id, Principal principal) {
		boolean success = carService.lockCar(id, principal.getName());

		if (!success) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					ApiResponse.<Boolean>builder()
							.success(false)
							.message("Vehicle is currently reserved by another user.")
							.data(false)
							.timestamp(LocalDateTime.now())
							.build()
			);
		}

		return ResponseEntity.ok(
				ApiResponse.<Boolean>builder()
						.success(true)
						.message("Vehicle locked successfully.")
						.data(true)
						.timestamp(LocalDateTime.now())
						.build()
		);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<CarResponse>> createCar(@Valid @RequestBody CarRequest request) {
		// FIXED: Removed the extra '>' after CarResponse
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<CarResponse>builder()
				.success(true)
				.data(carService.createCar(request))
				.timestamp(LocalDateTime.now())
				.build());
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<CarResponse>> updateCar(@PathVariable Long id, @Valid @RequestBody CarRequest request) {
		// FIXED: Removed the extra '>' after CarResponse
		return ResponseEntity.ok(ApiResponse.<CarResponse>builder()
				.success(true)
				.data(carService.updateCar(id, request))
				.timestamp(LocalDateTime.now())
				.build());
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteCar(@PathVariable Long id) {
		carService.deleteCar(id);
		return ResponseEntity.ok(ApiResponse.<Void>builder()
				.success(true)
				.timestamp(LocalDateTime.now())
				.build());
	}
}