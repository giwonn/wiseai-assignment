package com.wiseaiassignment.domain.reservation;

import com.wiseaiassignment.domain.common.exception.DomainException;
import com.wiseaiassignment.domain.common.exception.ExceptionType;
import com.wiseaiassignment.domain.reservation.model.*;
import com.wiseaiassignment.domain.reservation.repository.ReservationAttendeeRepository;
import com.wiseaiassignment.domain.reservation.repository.ReservationRepository;
import com.wiseaiassignment.domain.reservation.repository.ReservationSlotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final ReservationSlotRepository reservationSlotRepository;
	private final ReservationAttendeeRepository reservationAttendeeRepository;

	public List<ReservationSummary> findByDate(LocalDate date) {
		List<Reservation> reservations = reservationRepository.findByStatusAndDate(
				ReservationStatus.RESERVED,
				date.atStartOfDay(),
				date.plusDays(1).atStartOfDay()
		);

		return reservations.stream()
				.map(ReservationSummary::from)
				.toList();
	}

	public ReservationDetail findByIdWithAttendees(long id) {
		Reservation reservation = reservationRepository.findByIdWithAttendees(id)
				.orElseThrow(() -> new DomainException(ExceptionType.NOT_FOUND_RESERVATION));
		return ReservationDetail.from(reservation);
	}

	public void checkConflictReservation(Reservation reservation) {
		List<Reservation> conflictReservations = reservationRepository.findConflictReservations(
				reservation.getMeetingRoomId(),
				reservation.getStartTime(),
				reservation.getEndTime()
		);
		if (!conflictReservations.isEmpty()) throw new DomainException(ExceptionType.MEETING_ROOM_ALREADY_RESERVED);
	}

	@Transactional
	public ReservationDetail reserve(Reservation reservation, List<Long> attendeeUserIds) {
		reservationRepository.save(reservation);

		List<ReservationSlot> slots = ReservationSlot.createBulk(reservation);
		try {
			reservationSlotRepository.saveAll(slots);
		} catch (Exception ex) {
			throw new DomainException(ExceptionType.MEETING_ROOM_ALREADY_RESERVED);
		}

		List<ReservationAttendee> attendees = ReservationAttendee.bulkCreate(reservation, attendeeUserIds);
		reservationAttendeeRepository.saveAll(attendees);

		return ReservationDetail.from(reservation, attendees);
	}

	@Transactional
	public ReservationDetail cancel(long id, long userId) {
		Reservation reservation = reservationRepository.findByIdWithAttendees(id)
				.orElseThrow(() -> new DomainException(ExceptionType.NOT_FOUND_RESERVATION));

		reservation.checkValidOrganizer(userId);
		reservationSlotRepository.deleteByReservationId(reservation.getId());
		reservation.cancel();

		return ReservationDetail.from(reservation);
	}

	@Transactional
	public ReservationDetail changeTime(
			long reservationId,
			long userId,
			long meetingRoomId,
			LocalDateTime startTime,
			LocalDateTime endTime) {
		Reservation reservation = reservationRepository.findByIdWithAttendees(reservationId)
				.orElseThrow(() -> new DomainException(ExceptionType.NOT_FOUND_RESERVATION));

		reservation.checkValidOrganizer(userId);

		List<Reservation> conflictReservations = reservationRepository.findConflictReservations(
				reservation.getMeetingRoomId(),
				reservation.getStartTime(),
				reservation.getEndTime()
		);
		if (!conflictReservations.isEmpty()) throw new DomainException(ExceptionType.MEETING_ROOM_ALREADY_RESERVED);

		reservation.changeReservation(meetingRoomId, startTime, endTime);
		reservationSlotRepository.deleteByReservationId(reservation.getId());

		try {
			reservationSlotRepository.saveAll(ReservationSlot.createBulk(reservation));
		} catch (Exception ex) {
			throw new DomainException(ExceptionType.MEETING_ROOM_ALREADY_RESERVED);
		}

		return ReservationDetail.from(reservation);
	}


}
