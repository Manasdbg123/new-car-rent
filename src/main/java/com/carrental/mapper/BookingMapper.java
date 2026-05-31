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
				.carName(
						booking.getCar().getBrand() +
								" " +
								booking.getCar().getModel()
				)
				.carImage(booking.getCar().getImageUrl())
				.startDate(booking.getStartDate())
				.endDate(booking.getEndDate())
				.totalPrice(booking.getTotalPrice())
				.status(booking.getStatus().name())
				.cancellationReason(booking.getCancellationReason())
				.cancelledAt(booking.getCancelledAt())
				.createdAt(booking.getCreatedAt())
				.build();
	}
}