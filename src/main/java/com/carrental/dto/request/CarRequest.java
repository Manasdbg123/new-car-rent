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
	@Size(max = 100, message = "Brand cannot exceed 100 characters")
	private String brand;

	@NotBlank(message = "Model is required")
	@Size(max = 100, message = "Model cannot exceed 100 characters")
	private String model;

	@NotBlank(message = "Car type is required")
	@Size(max = 50, message = "Type cannot exceed 50 characters")
	private String type;

	@NotNull(message = "Manufacturing year is required")
	@Min(value = 2000, message = "Year must be greater than or equal to 2000")
	@Max(value = 2100, message = "Invalid manufacturing year")
	private Integer year;

	@NotBlank(message = "Registration number is required")
	@Size(max = 30, message = "Registration number cannot exceed 30 characters")
	private String registrationNumber;

	@NotNull(message = "Price per day is required")
	@DecimalMin(value = "1.0", message = "Price per day must be greater than 0")
	private BigDecimal pricePerDay;

	@NotNull(message = "Seating capacity is required")
	@Positive(message = "Seating capacity must be positive")
	private Integer seatingCapacity;

	@NotBlank(message = "Fuel type is required")
	private String fuelType;

	@NotBlank(message = "Transmission type is required")
	private String transmission;

	@NotBlank(message = "City is required")
	@Size(max = 100, message = "City cannot exceed 100 characters")
	private String city;

	@NotBlank(message = "Image URL is required")
	@Size(max = 1000, message = "Image URL cannot exceed 1000 characters")
	private String imageUrl;

	@Size(max = 2000, message = "Description cannot exceed 2000 characters")
	private String description;

	private Boolean available;
}