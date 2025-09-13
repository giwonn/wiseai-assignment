package com.wiseaiassignment.domain.notification.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 회의 알림에 필요한 데이터를 담는 DTO
 */
public record MeetingNotificationData(
		long reservationId,
		String meetingTitle,
		String meetingRoomName,
		String organizerName,
		String organizerEmail,
		LocalDateTime startTime,
		LocalDateTime endTime,
		List<String> attendeeEmails,
		String additionalMessage
) {

	public static MeetingNotificationData of(
			long reservationId,
			String meetingTitle,
			String meetingRoomName,
			String organizerName,
			String organizerEmail,
			LocalDateTime startTime,
			LocalDateTime endTime,
			List<String> attendeeEmails
	) {
		return new MeetingNotificationData(
				reservationId,
				meetingTitle,
				meetingRoomName,
				organizerName,
				organizerEmail,
				startTime,
				endTime,
				attendeeEmails,
				null
		);
	}

	public MeetingNotificationData withMessage(String message) {
		return new MeetingNotificationData(
				reservationId,
				meetingTitle,
				meetingRoomName,
				organizerName,
				organizerEmail,
				startTime,
				endTime,
				attendeeEmails,
				message
		);
	}
}
