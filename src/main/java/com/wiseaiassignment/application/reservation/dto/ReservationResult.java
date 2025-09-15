package com.wiseaiassignment.application.reservation.dto;

import com.wiseaiassignment.domain.meetingroom.model.MeetingRoom;
import com.wiseaiassignment.domain.reservation.model.ReservationStatus;
import com.wiseaiassignment.domain.reservation.model.ReservationDetail;

import java.time.LocalDateTime;
import java.util.List;

public record ReservationResult(
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
	public static ReservationResult from(ReservationDetail detail, MeetingRoom meetingRoom) {
		return new ReservationResult(
				detail.reservationId(),
				detail.reservationTitle(),
				detail.status(),
				detail.meetingRoomId(),
				meetingRoom.getName(),
				detail.startTime(),
				detail.endTime(),
				detail.organizerId(),
				detail.attendeeUserIds()
		);
	}
}
