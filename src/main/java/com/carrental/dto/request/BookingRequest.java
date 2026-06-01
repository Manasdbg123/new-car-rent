package com.carrental.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

	@NotNull(message = "Car ID is required")
	private Long carId;

	@NotNull(message = "Start date and time is required")
	@FutureOrPresent(message = "Start date must be in the present or future")
	@JsonProperty("startAt")
	@JsonAlias({"startDate", "start_at"})
	private LocalDateTime startAt;

	@NotNull(message = "End date and time is required")
	@Future(message = "End date must be in the future")
	@JsonProperty("endAt")
	@JsonAlias({"endDate", "end_at"})
	private LocalDateTime endAt;
}