package com.carrental.mapper;

import com.carrental.dto.response.BookingResponse;
import com.carrental.entity.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

	public BookingResponse toResponse(Booking booking) {

		if (booking == null) {
			return null;
		}

		return BookingResponse.builder()
				.id(booking.getId())
				.userId(booking.getUser().getId())
				.userName(booking.getUser().getFullName())
				.carId(booking.getCar().getId())
				.carBrand(booking.getCar().getBrand())
				.carModel(booking.getCar().getModel())
				.carImage(booking.getCar().getImageUrl())
				.startAt(booking.getStartAt())
				.endAt(booking.getEndAt())
				.totalAmount(booking.getTotalAmount())
				.status(booking.getStatus().name())
				.cancellationReason(booking.getCancellationReason())
				.cancelledAt(booking.getCancelledAt())
				.createdAt(booking.getCreatedAt())
				.build();
	}
}