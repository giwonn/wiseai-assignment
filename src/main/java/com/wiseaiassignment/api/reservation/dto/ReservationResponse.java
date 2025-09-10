package com.wiseaiassignment.api.reservation.dto;

import java.time.LocalDateTime;

public record ReservationResponse(
		long reservationId,
		long roomId,
		String roomName,
		String reserverName,
		String reserverEmail,
		LocalDateTime startTime,
		LocalDateTime endTime
) {

}
