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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
		name = "bookings",
		indexes = {
				@Index(name = "idx_booking_user", columnList = "user_id"),
				@Index(name = "idx_booking_car", columnList = "car_id"),
				@Index(name = "idx_booking_status", columnList = "status"),
				@Index(name = "idx_booking_dates", columnList = "startDate,endDate")
		}
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private AppUser user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "car_id", nullable = false)
	private Car car;

	@Column(nullable = false)
	private LocalDate startDate;

	@Column(nullable = false)
	private LocalDate endDate;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal totalPrice;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private BookingStatus status;

	@Column(length = 500)
	private String cancellationReason;

	private LocalDateTime cancelledAt;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	public long getTotalDays() {
		return startDate.until(endDate).getDays();
	}
}