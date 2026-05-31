package com.carrental.repository;

import com.carrental.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long>, JpaSpecificationExecutor<Car> {

	List<Car> findByAvailableTrue();

	Page<Car> findByAvailableTrue(Pageable pageable);

	Page<Car> findByCityIgnoreCaseAndAvailableTrue(String city, Pageable pageable);

	Page<Car> findByBrandContainingIgnoreCaseAndAvailableTrue(String brand, Pageable pageable);

	Page<Car> findByTypeIgnoreCaseAndAvailableTrue(String type, Pageable pageable);

	Page<Car> findByPricePerDayBetweenAndAvailableTrue(
			BigDecimal minPrice,
			BigDecimal maxPrice,
			Pageable pageable
	);

	boolean existsByRegistrationNumber(String registrationNumber);
}