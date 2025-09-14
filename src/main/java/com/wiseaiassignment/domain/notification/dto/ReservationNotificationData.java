package com.wiseaiassignment.domain.notification.dto;

import com.wiseaiassignment.domain.reservation.model.Reservation;
import com.wiseaiassignment.domain.reservation.model.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 회의 알림에 필요한 데이터를 담는 DTO
 */
public record ReservationNotificationData(
		long reservationId,
		String meetingTitle,
		String meetingRoomName,
		String organizerEmail,
		LocalDateTime startTime,
		LocalDateTime endTime,
		List<String> attendeeEmails,
		ReservationStatus status
) {
	public static ReservationNotificationData from(Reservation reservation) {
		return new ReservationNotificationData(
				reservation.getId(),
				reservation.getTitle(),
				reservation.getMeetingRoomName(),
				reservation.getUserEmail(),
				reservation.getStartTime(),
				reservation.getEndTime(),
				reservation.getAttendeeEmails(),
				reservation.getStatus()
		);
	}
}
