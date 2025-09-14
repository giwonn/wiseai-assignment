package com.wiseaiassignment.application.reservation.dto;

import com.wiseaiassignment.domain.meetingroom.model.MeetingRoom;
import com.wiseaiassignment.domain.reservation.model.Reservation;
import com.wiseaiassignment.domain.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public record CreateReservationCommand(
		String title,
		Long userId,
		Long meetingRoomId,
		LocalDateTime startTime,
		LocalDateTime endTime,
		List<String> attendeeEmails
) {
	public Reservation toEntity(User user, MeetingRoom meetingRoom) {
		return Reservation.create(
				title,
				userId,
				user.getEmail(),
				meetingRoomId,
				meetingRoom.getName(),
				startTime,
				endTime,
				attendeeEmails
		);
	}
}
