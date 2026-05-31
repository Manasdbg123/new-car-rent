package com.carrental.controller;

import com.carrental.dto.request.CarRequest;
import com.carrental.dto.response.ApiResponse;
import com.carrental.dto.response.CarResponse;
import com.carrental.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/cars")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCarController {

	private final CarService carService;

	@PostMapping
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