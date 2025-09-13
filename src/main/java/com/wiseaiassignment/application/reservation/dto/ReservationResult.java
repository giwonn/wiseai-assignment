package com.wiseaiassignment.application.reservation.dto;

import com.wiseaiassignment.domain.meetingroom.model.MeetingRoom;
import com.wiseaiassignment.domain.reservation.model.Reservation;
import com.wiseaiassignment.domain.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public record ReservationResult(
		long reservationId,
		String reservationTitle,
		long roomId,
		String roomName,
		String reserverName,
		String reserverEmail,
		LocalDateTime startTime,
		LocalDateTime endTime,
		List<String> attendeeEmails
) {
	public static ReservationResult from(User user, MeetingRoom meetingRoom, Reservation reservation) {
		return new ReservationResult(
				reservation.getId(),
				reservation.getTitle(),
				reservation.getMeetingRoomId(),
				meetingRoom.getName(),
				user.getName(),
				user.getEmail(),
				reservation.getStartTime(),
				reservation.getEndTime(),
				reservation.getAttendeeEmails()
		);
	}
}
