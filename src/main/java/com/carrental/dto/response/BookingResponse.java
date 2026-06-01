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
public class BookingResponse {

	private Long id;

	private Long userId;

	private String userName;

	private Long carId;

	private String carBrand;

	private String carModel;

	private String carImage;

	private LocalDateTime startAt;

	private LocalDateTime endAt;

	private BigDecimal totalAmount;

	private String status;

	private String cancellationReason;

	private LocalDateTime cancelledAt;

	private LocalDateTime createdAt;
}