package com.wiseaiassignment.application.reservation;

import com.wiseaiassignment.application.reservation.dto.*;
import com.wiseaiassignment.domain.meetingroom.MeetingRoomService;
import com.wiseaiassignment.domain.meetingroom.model.MeetingRoom;
import com.wiseaiassignment.domain.notification.NotificationService;
import com.wiseaiassignment.domain.notification.dto.ReservationNotificationData;
import com.wiseaiassignment.domain.reservation.ReservationService;
import com.wiseaiassignment.domain.reservation.model.Reservation;
import com.wiseaiassignment.domain.reservation.model.ReservationDetail;
import com.wiseaiassignment.domain.reservation.model.ReservationSummary;
import com.wiseaiassignment.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationAppService {

	private final UserService userService;
	private final MeetingRoomService meetingRoomService;
	private final ReservationService reservationService;
	private final NotificationService notificationService;

	public List<ReservationSummaryResult> findByDate(LocalDate date) {
		List<ReservationSummary> reservations = reservationService.findByDate(date);
		return reservations.stream().map(ReservationSummaryResult::from).toList();
	}

	public ReservationResult findById(long id) {
		ReservationDetail reservation = reservationService.findByIdWithAttendees(id);
		MeetingRoom meetingRoom = meetingRoomService.findById(reservation.meetingRoomId());
		return ReservationResult.from(reservation, meetingRoom);
	}

	public ReservationResult reserve(CreateReservationCommand command) {
		// 1. 사용자와 회의실이 존재하는지 확인
		userService.findActiveUserById(command.userId());
		MeetingRoom meetingRoom = meetingRoomService.findActiveMeetingRoomById(command.meetingRoomId());

		// 2. 예약 진행
		Reservation reservation = command.toEntity();
		reservationService.checkConflictReservation(reservation);
		ReservationDetail detail = reservationService.reserve(reservation, command.attendeeUserIds());

		// 3. 예약 이메일 전송 (비동기)
		notificationService.sendEmail(ReservationNotificationData.from(detail, meetingRoom));

		return ReservationResult.from(detail, meetingRoom);
	}

	public void cancel(CancelReservationCommand command) {
		// 1. 예약 취소
		MeetingRoom meetingRoom = meetingRoomService.findActiveMeetingRoomById(command.reservationId());
		ReservationDetail reservation = reservationService.cancel(command.reservationId(), command.userId());

		// 2. 취소 이메일 전송 (비동기)
		notificationService.sendEmail(ReservationNotificationData.from(reservation, meetingRoom));
	}

	public ReservationResult changeReservation(ChangeReservationCommand command) {
		ReservationDetail reservation = reservationService.changeTime(
				command.reservationId(),
				command.userId(),
				command.roomId(),
				command.startTime(),
				command.endTime()
		);

		MeetingRoom meetingRoom = meetingRoomService.findById(command.reservationId());
		notificationService.sendEmail(ReservationNotificationData.from(reservation, meetingRoom));

		return ReservationResult.from(reservation, meetingRoom);
	}

}
