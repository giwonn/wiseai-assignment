package com.wiseaiassignment.api.reservation.dto;

import com.wiseaiassignment.application.reservation.dto.ReservationResult;
import com.wiseaiassignment.domain.reservation.model.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ReservationResponse(
		long reservationId,
		String reservationTitle,
		ReservationStatus status,
		long roomId,
		String roomName,
		String reserverEmail,
		LocalDateTime startTime,
		LocalDateTime endTime,
		List<String> attendeeEmails
) {

	public static ReservationResponse from(ReservationResult result) {
		return new ReservationResponse(
				result.reservationId(),
				result.reservationTitle(),
				result.status(),
				result.roomId(),
				result.roomName(),
				result.reserverEmail(),
				result.startTime(),
				result.endTime(),
				result.attendeeEmails()
		);
	}
}
