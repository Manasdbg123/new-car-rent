package com.carrental.repository;

import com.carrental.entity.Booking;
import com.carrental.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);

	List<Booking> findByCarIdOrderByStartAtDesc(Long carId);

	List<Booking> findByStatus(BookingStatus status);

	Optional<Booking> findByIdAndUserId(Long bookingId, Long userId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("""
            SELECT b
            FROM Booking b
            WHERE b.car.id = :carId
            AND b.status IN ('PENDING', 'CONFIRMED')
            AND (
                    :startAt <= b.endAt
                    AND :endAt >= b.startAt
                )
            """)
	List<Booking> findConflictingBookings(
			@Param("carId") Long carId,
			@Param("startAt") LocalDateTime startAt,
			@Param("endAt") LocalDateTime endAt
	);

	@Query("""
            SELECT COUNT(b) > 0
            FROM Booking b
            WHERE b.car.id = :carId
            AND b.status IN ('PENDING', 'CONFIRMED')
            AND (
                    :startAt <= b.endAt
                    AND :endAt >= b.startAt
                )
            """)
	boolean existsActiveBookingConflict(
			@Param("carId") Long carId,
			@Param("startAt") LocalDateTime startAt,
			@Param("endAt") LocalDateTime endAt
	);
}