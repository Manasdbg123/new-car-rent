package com.carrental.repository;

import com.carrental.entity.Car;
import com.carrental.entity.CarStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

	// Replaces the old findByAvailableTrue method
	Page<Car> findByStatus(CarStatus status, Pageable pageable);

	// Replaces the old existsByRegistrationNumber method
	boolean existsByLicensePlate(String licensePlate);
}