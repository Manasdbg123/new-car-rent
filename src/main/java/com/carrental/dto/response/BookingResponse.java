package com.carrental.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

	private Long id;

	private Long userId;

	private String userName;

	private Long carId;

	private String carName;

	private String carImage;

	private LocalDate startDate;

	private LocalDate endDate;

	private BigDecimal totalPrice;

	private String status;

	private String cancellationReason;

	private LocalDateTime cancelledAt;

	private LocalDateTime createdAt;
}