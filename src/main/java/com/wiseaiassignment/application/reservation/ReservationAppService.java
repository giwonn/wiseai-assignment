package com.wiseaiassignment.application.reservation;

import com.wiseaiassignment.application.reservation.dto.CreateReservationCommand;
import com.wiseaiassignment.application.reservation.dto.ReservationResult;
import com.wiseaiassignment.domain.meetingroom.model.MeetingRoom;
import com.wiseaiassignment.domain.meetingroom.MeetingRoomService;
import com.wiseaiassignment.domain.notification.NotificationService;
import com.wiseaiassignment.domain.notification.dto.MeetingNotificationData;
import com.wiseaiassignment.domain.reservation.ReservationService;
import com.wiseaiassignment.domain.reservation.model.Reservation;
import com.wiseaiassignment.domain.user.model.User;
import com.wiseaiassignment.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationAppService {

	private final UserService userService;
	private final MeetingRoomService meetingRoomService;
	private final ReservationService reservationService;
	private final NotificationService notificationService;

	public void find(Reservation reservation) {
		// 예약 로직 구현

		// 1.
	}

	public ReservationResult reserve(CreateReservationCommand command) {
		// 1. 사용자와 회의실이 존재하는지 확인
		User user = userService.findActiveUserById(command.userId());
		MeetingRoom meetingRoom = meetingRoomService.findActiveMeetingRoomById(command.meetingRoomId());

		// 2. 예약 진행
		Reservation reservation = reservationService.reserve(command.toEntity());

		// 3. 예약 이메일 전송 (비동기)
		notificationService.sendEmail(
				MeetingNotificationData.of(
					reservation.getId(),
					reservation.getTitle(),
					meetingRoom.getName(),
					user.getName(),
					user.getEmail(),
					reservation.getStartTime(),
					reservation.getEndTime(),
					reservation.getAttendeeEmails()
				)
		);

		return ReservationResult.from(user, meetingRoom, reservation);
	}

}
