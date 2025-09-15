package com.wiseaiassignment.domain.notification.dto;

import com.wiseaiassignment.domain.meetingroom.model.MeetingRoom;
import com.wiseaiassignment.domain.reservation.model.ReservationDetail;
import com.wiseaiassignment.domain.reservation.model.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ReservationNotificationData(
		long reservationId,
		String meetingTitle,
		long meetingRoomId,
		String meetingRoomName,
		long organizerId,
		LocalDateTime startTime,
		LocalDateTime endTime,
		ReservationStatus status,
		List<Long> attendeeIds
) {
	public static ReservationNotificationData from(ReservationDetail reservation, MeetingRoom meetingRoom) {
		return new ReservationNotificationData(
				reservation.reservationId(),
				reservation.reservationTitle(),
				reservation.meetingRoomId(),
				meetingRoom.getName(),
				reservation.organizerId(),
				reservation.startTime(),
				reservation.endTime(),
				reservation.status(),
				reservation.attendeeUserIds()
		);
	}
}
