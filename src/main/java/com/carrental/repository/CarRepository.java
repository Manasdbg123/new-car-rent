package com.carrental.repository;

import com.carrental.entity.Car;
import com.carrental.entity.CarStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

	Page<Car> findByStatus(CarStatus status, Pageable pageable);

	boolean existsByLicensePlate(String licensePlate);

	// UPDATED: Now filters out cars that are currently locked by an active checkout session
	@Query("SELECT c FROM Car c WHERE c.status = :status " +
			"AND (c.lockedUntil IS NULL OR c.lockedUntil < CURRENT_TIMESTAMP) " +
			"AND (:city IS NULL OR c.city = :city) " +
			"AND (:brand IS NULL OR c.brand = :brand) " +
			"AND (:vehicleType IS NULL OR c.vehicleType = :vehicleType) " +
			"AND (:transmission IS NULL OR c.transmission = :transmission) " +
			"AND (:fuelType IS NULL OR c.fuelType = :fuelType) " +
			"AND (:minPrice IS NULL OR c.dailyRate >= :minPrice) " +
			"AND (:maxPrice IS NULL OR c.dailyRate <= :maxPrice)")
	Page<Car> searchAvailableCars(
			@Param("status") CarStatus status,
			@Param("city") String city,
			@Param("brand") String brand,
			@Param("vehicleType") String vehicleType,
			@Param("transmission") String transmission,
			@Param("fuelType") String fuelType,
			@Param("minPrice") BigDecimal minPrice,
			@Param("maxPrice") BigDecimal maxPrice,
			Pageable pageable
	);
}