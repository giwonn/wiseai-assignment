package com.wiseaiassignment.api.reservation.dto;

import com.wiseaiassignment.application.reservation.dto.ReservationSummaryResult;
import com.wiseaiassignment.domain.reservation.model.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationSummaryResponse(
		long reservationId,
		String reservationTitle,
		long roomId,
		LocalDateTime startTime,
		LocalDateTime endTime,
		ReservationStatus status
) {
	public static ReservationSummaryResponse from(ReservationSummaryResult result) {
		return new ReservationSummaryResponse(
				result.reservationId(),
				result.reservationTitle(),
				result.roomId(),
				result.startTime(),
				result.endTime(),
				result.status()
		);
	}
}
