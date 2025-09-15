package com.wiseaiassignment.domain.reservation.model;

import java.time.LocalDateTime;
import java.util.List;

public record ReservationDetail(
	long reservationId,
	String reservationTitle,
	ReservationStatus status,
	long meetingRoomId,
	LocalDateTime startTime,
	LocalDateTime endTime,
	long organizerId,
	List<Long> attendeeUserIds
) {
	public static ReservationDetail from(Reservation reservation) {
		return new ReservationDetail(
				reservation.getId(),
				reservation.getTitle(),
				reservation.getStatus(),
				reservation.getMeetingRoomId(),
				reservation.getStartTime(),
				reservation.getEndTime(),
				reservation.getOrganizerId(),
				reservation.getAttendeeUserIds()
		);
	}

	public static ReservationDetail from(Reservation reservation, List<ReservationAttendee> attendees) {
		return new ReservationDetail(
				reservation.getId(),
				reservation.getTitle(),
				reservation.getStatus(),
				reservation.getMeetingRoomId(),
				reservation.getStartTime(),
				reservation.getEndTime(),
				reservation.getOrganizerId(),
				attendees.stream().map(ReservationAttendee::getUserId).toList()
		);
	}
}
