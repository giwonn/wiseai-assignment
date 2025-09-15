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
		try {
			reservationRepository.save(reservation);

			List<ReservationSlot> slots = ReservationSlot.createBulk(reservation);
			reservationSlotRepository.saveAll(slots);

			List<ReservationAttendee> attendees = ReservationAttendee.bulkCreate(reservation, attendeeUserIds);
			reservationAttendeeRepository.saveAll(attendees);

			return ReservationDetail.from(reservation, attendees);
		} catch (Exception ex) {
			log.error("회의실 예약 예외 발생 : {}", ex.getMessage());
			throw new DomainException(ExceptionType.RESERVATION_FAILED);
		}
	}

	@Transactional
	public ReservationDetail cancel(long id, long userId) {
		Reservation reservation = reservationRepository.findByIdWithAttendees(id)
				.orElseThrow(() -> new DomainException(ExceptionType.NOT_FOUND_RESERVATION));

		reservationSlotRepository.deleteByReservationId(reservation.getId());
		reservation.cancel(userId);

		return ReservationDetail.from(reservation);
	}
}
