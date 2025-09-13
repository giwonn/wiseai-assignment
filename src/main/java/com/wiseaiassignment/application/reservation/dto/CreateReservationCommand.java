package com.wiseaiassignment.application.reservation.dto;

import com.wiseaiassignment.domain.reservation.model.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public record CreateReservationCommand(
		String title,
		Long userId,
		Long meetingRoomId,
		LocalDateTime startTime,
		LocalDateTime endTime,
		List<String> attendeeEmails
) {
	public Reservation toEntity() {
		return Reservation.create(
				title,
				userId,
				meetingRoomId,
				startTime,
				endTime,
				attendeeEmails
		);
	}
}
