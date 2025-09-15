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
		LocalDateTime startTime,
		LocalDateTime endTime,
		long organizerId,
		List<Long> attendeeUserIds
) {

	public static ReservationResponse from(ReservationResult result) {
		return new ReservationResponse(
				result.reservationId(),
				result.reservationTitle(),
				result.status(),
				result.roomId(),
				result.roomName(),
				result.startTime(),
				result.endTime(),
				result.organizerId(),
				result.attendeeUserIds()
		);
	}
}
