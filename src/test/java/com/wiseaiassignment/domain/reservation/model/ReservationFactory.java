package com.wiseaiassignment.domain.reservation.model;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationFactory {

	public static Reservation create(
			Long id,
			String title,
			long meetingRoomId,
			long userId,
			LocalDateTime startTime,
			LocalDateTime endTime,
			List<String> emails
	) {
		return new Reservation(
				id,
				title,
				meetingRoomId,
				userId,
				TimeRange.of(startTime, endTime),
				emails
		);
	}
}
