package com.wiseaiassignment.application.reservation.dto;

import java.time.LocalDateTime;

public record ChangeReservationCommand(
		long reservationId,
		long userId,
		long roomId,
		LocalDateTime startTime,
		LocalDateTime endTime
) {
}
