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
	private Integer manufactureYear;
	private String licensePlate;
	private BigDecimal dailyRate;
	private Integer seats;
	private String fuelType;
	private String transmission;
	private String city;
	private String imageUrl;
	private String description;
	private String status;
	private LocalDateTime createdAt;
}