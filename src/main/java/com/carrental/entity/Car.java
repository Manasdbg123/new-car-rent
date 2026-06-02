package com.carrental.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String brand;
	private String model;

	// FIXED: Added the missing manufactureYear field back!
	private Integer manufactureYear;

	private String transmission;
	private String city;
	private Integer seats;
	private String fuelType;
	private String vehicleType;

	@Column(columnDefinition = "TEXT")
	private String description;

	private String imageUrl;
	private BigDecimal dailyRate;

	@Column(unique = true)
	private String licensePlate;

	@Enumerated(EnumType.STRING)
	private CarStatus status;

	// Concurrency & Lock tracking
	@Column(name = "locked_until")
	private LocalDateTime lockedUntil;

	@Column(name = "locked_by")
	private String lockedBy;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;
}