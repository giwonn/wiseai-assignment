package com.wiseaiassignment.domain.reservation.model;

import java.time.LocalDateTime;

public class ReservationFactory {


	public static Reservation create(
			Long id,
			String title,
			LocalDateTime startTime,
			LocalDateTime endTime
	) {
		return new Reservation(
				id,
				title,
				1L,
				1L,
				TimeRange.of(startTime, endTime)
		);
	}


	public static Reservation create(
			Long id,
			String title,
			long meetingRoomId,
			long organizerId,
			LocalDateTime startTime,
			LocalDateTime endTime
	) {
		return new Reservation(
				id,
				title,
				meetingRoomId,
				organizerId,
				TimeRange.of(startTime, endTime)
		);
	}
}
