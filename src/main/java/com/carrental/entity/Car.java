package com.carrental.entity;

import jakarta.persistence.*;
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
				@Index(name = "idx_cars_brand", columnList = "brand"),
				@Index(name = "idx_cars_model", columnList = "model"),
				@Index(name = "idx_cars_city", columnList = "city"),
				@Index(name = "idx_cars_status", columnList = "status")
		}
)
public class Car {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "vehicle_type", nullable = false, length = 30)
	private String vehicleType; // "CAR" or "BIKE"

	@Column(nullable = false, length = 80)
	private String brand;

	@Column(nullable = false, length = 80)
	private String model;

	@Column(name = "manufacture_year", nullable = false)
	private Integer manufactureYear;

	@Column(name = "license_plate", nullable = false, unique = true, length = 30)
	private String licensePlate;

	@Column(name = "daily_rate", nullable = false, precision = 12, scale = 2)
	private BigDecimal dailyRate;

	@Column(nullable = false)
	private Integer seats;

	@Column(name = "fuel_type", nullable = false, length = 60)
	private String fuelType;

	@Column(nullable = false, length = 60)
	private String transmission;

	@Column(nullable = false, length = 60)
	private String city;

	@Column(name = "image_url", length = 1000)
	private String imageUrl;

	@Column(length = 500)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private CarStatus status;

	@Version
	private Long version;

	@OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Booking> bookings = new ArrayList<>();

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	// Constructors
	public Car() {
	}

	// ==========================================
	// EXPLICIT GETTERS & SETTERS (LOMBOK BYPASS)
	// ==========================================

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getVehicleType() { return vehicleType; }
	public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

	public String getBrand() { return brand; }
	public void setBrand(String brand) { this.brand = brand; }

	public String getModel() { return model; }
	public void setModel(String model) { this.model = model; }

	public Integer getManufactureYear() { return manufactureYear; }
	public void setManufactureYear(Integer manufactureYear) { this.manufactureYear = manufactureYear; }

	public String getLicensePlate() { return licensePlate; }
	public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

	public BigDecimal getDailyRate() { return dailyRate; }
	public void setDailyRate(BigDecimal dailyRate) { this.dailyRate = dailyRate; }

	public Integer getSeats() { return seats; }
	public void setSeats(Integer seats) { this.seats = seats; }

	public String getFuelType() { return fuelType; }
	public void setFuelType(String fuelType) { this.fuelType = fuelType; }

	public String getTransmission() { return transmission; }
	public void setTransmission(String transmission) { this.transmission = transmission; }

	public String getCity() { return city; }
	public void setCity(String city) { this.city = city; }

	public String getImageUrl() { return imageUrl; }
	public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public CarStatus getStatus() { return status; }
	public void setStatus(CarStatus status) { this.status = status; }

	public Long getVersion() { return version; }
	public void setVersion(Long version) { this.version = version; }

	public List<Booking> getBookings() { return bookings; }
	public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

	public LocalDateTime getCreatedAt() { return createdAt; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

	public LocalDateTime getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}