package com.carrental.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CarRequest {

	@NotBlank(message = "Brand is required")
	@Size(max = 80, message = "Brand cannot exceed 80 characters")
	private String brand;

	@NotBlank(message = "Model is required")
	@Size(max = 80, message = "Model cannot exceed 80 characters")
	private String model;

	@NotNull(message = "Manufacturing year is required")
	@Min(value = 1980, message = "Year must be greater than or equal to 1980")
	@Max(value = 2100, message = "Invalid manufacturing year")
	private Integer manufactureYear;

	@NotBlank(message = "License plate is required")
	@Size(max = 30, message = "License plate cannot exceed 30 characters")
	private String licensePlate;

	@NotNull(message = "Daily rate is required")
	@DecimalMin(value = "1.0", message = "Daily rate must be greater than 0")
	private BigDecimal dailyRate;

	@NotNull(message = "Seating capacity is required")
	@Positive(message = "Seating capacity must be positive")
	private Integer seats;

	@NotBlank(message = "Fuel type is required")
	private String fuelType;

	@NotBlank(message = "Transmission type is required")
	private String transmission;

	@NotBlank(message = "City is required")
	@Size(max = 60, message = "City cannot exceed 60 characters")
	private String city;

	@NotBlank(message = "Image URL is required")
	@Size(max = 1000, message = "Image URL cannot exceed 1000 characters")
	private String imageUrl;

	@Size(max = 500, message = "Description cannot exceed 500 characters")
	private String description;

	private String status;
}