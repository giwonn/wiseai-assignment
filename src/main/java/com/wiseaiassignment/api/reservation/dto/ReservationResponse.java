package com.wiseaiassignment.api.reservation.dto;

import com.wiseaiassignment.application.reservation.dto.ReservationResult;

import java.time.LocalDateTime;
import java.util.List;

public record ReservationResponse(
		long reservationId,
		String reservationTitle,
		long roomId,
		String roomName,
		String reserverName,
		String reserverEmail,
		LocalDateTime startTime,
		LocalDateTime endTime,
		List<String> attendeeEmails
) {

	public static ReservationResponse from(ReservationResult result) {
		return new ReservationResponse(
				result.reservationId(),
				result.reservationTitle(),
				result.roomId(),
				result.roomName(),
				result.reserverName(),
				result.reserverEmail(),
				result.startTime(),
				result.endTime(),
				result.attendeeEmails()
		);
	}
}
