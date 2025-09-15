package com.wiseaiassignment.application.reservation.dto;

import com.wiseaiassignment.domain.reservation.model.ReservationStatus;
import com.wiseaiassignment.domain.reservation.model.ReservationSummary;

import java.time.LocalDateTime;

public record ReservationSummaryResult(
		long reservationId,
		String reservationTitle,
		long roomId,
		LocalDateTime startTime,
		LocalDateTime endTime,
		ReservationStatus status
) {
	public static ReservationSummaryResult from(ReservationSummary reservation) {
		return new ReservationSummaryResult(
				reservation.id(),
				reservation.title(),
				reservation.meetingRoomId(),
				reservation.startTime(),
				reservation.endTime(),
				reservation.status()
		);
	}
}
