package com.carrental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
		name = "cars",
		indexes = {
				@Index(name = "idx_car_brand", columnList = "brand"),
				@Index(name = "idx_car_model", columnList = "model"),
				@Index(name = "idx_car_city", columnList = "city"),
				@Index(name = "idx_car_available", columnList = "available")
		}
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String brand;

	@Column(nullable = false, length = 100)
	private String model;

	@Column(nullable = false, length = 50)
	private String type;

	@Column(nullable = false)
	private Integer year;

	@Column(nullable = false, unique = true, length = 30)
	private String registrationNumber;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal pricePerDay;

	@Column(nullable = false)
	private Integer seatingCapacity;

	@Column(nullable = false)
	private String fuelType;

	@Column(nullable = false)
	private String transmission;

	@Column(nullable = false, length = 100)
	private String city;

	@Column(nullable = false, length = 1000)
	private String imageUrl;

	@Column(length = 2000)
	private String description;

	@Builder.Default
	@Column(nullable = false)
	private Boolean available = true;

	@OneToMany(
			mappedBy = "car",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	@Builder.Default
	private List<Booking> bookings = new ArrayList<>();

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	private LocalDateTime updatedAt;
}