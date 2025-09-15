package com.wiseaiassignment.application.reservation.dto;

import com.wiseaiassignment.domain.reservation.model.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public record CreateReservationCommand(
		String title,
		long userId,
		long meetingRoomId,
		LocalDateTime startTime,
		LocalDateTime endTime,
		List<Long> attendeeUserIds
) {
	public Reservation toEntity() {
		return Reservation.create(
				title,
				startTime,
				endTime,
				userId,
				meetingRoomId
		);
	}
}
