package com.wiseaiassignment.application.reservation.dto;

import com.wiseaiassignment.domain.reservation.model.Reservation;
import com.wiseaiassignment.domain.reservation.model.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationSummaryResult(
		long reservationId,
		String reservationTitle,
		long roomId,
		LocalDateTime startTime,
		LocalDateTime endTime,
		ReservationStatus status
) {
	public static ReservationSummaryResult from(Reservation reservation) {
		return new ReservationSummaryResult(
				reservation.getId(),
				reservation.getTitle(),
				reservation.getMeetingRoomId(),
				reservation.getStartTime(),
				reservation.getEndTime(),
				reservation.getStatus()
		);
	}
}
