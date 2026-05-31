package com.carrental.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarResponse {

	private Long id;

	private String brand;

	private String model;

	private String type;

	private Integer year;

	private String registrationNumber;

	private BigDecimal pricePerDay;

	private Integer seatingCapacity;

	private String fuelType;

	private String transmission;

	private String city;

	private String imageUrl;

	private String description;

	private Boolean available;

	private LocalDateTime createdAt;
}