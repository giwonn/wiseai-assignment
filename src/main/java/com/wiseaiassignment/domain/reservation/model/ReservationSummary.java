package com.wiseaiassignment.domain.reservation.model;

import java.time.LocalDateTime;

public record ReservationSummary(
		long id,
		String title,
		long meetingRoomId,
		LocalDateTime startTime,
		LocalDateTime endTime,
		ReservationStatus status
) {

	public static ReservationSummary from(Reservation reservation) {
		return new ReservationSummary(
				reservation.getId(),
				reservation.getTitle(),
				reservation.getMeetingRoomId(),
				reservation.getStartTime(),
				reservation.getEndTime(),
				reservation.getStatus()
		);
	}
}
