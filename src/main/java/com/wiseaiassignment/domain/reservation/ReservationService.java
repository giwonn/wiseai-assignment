package com.wiseaiassignment.domain.reservation;

import com.wiseaiassignment.domain.common.exception.DomainException;
import com.wiseaiassignment.domain.common.exception.ExceptionType;
import com.wiseaiassignment.domain.reservation.model.Reservation;
import com.wiseaiassignment.domain.reservation.model.ReservationSlot;
import com.wiseaiassignment.domain.reservation.model.ReservationStatus;
import com.wiseaiassignment.domain.reservation.model.ReservationSummary;
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

	public Reservation findById(long id) {
		return reservationRepository.findById(id)
				.orElseThrow(() -> new DomainException(ExceptionType.NOT_FOUND_RESERVATION));
	}

	@Transactional
	public Reservation reserve(Reservation reservation) {
			List<Reservation> conflictReservations = reservationRepository.findConflictReservations(
					reservation.getMeetingRoomId(),
					reservation.getStartTime(),
					reservation.getEndTime()
			);
			if (!conflictReservations.isEmpty()) throw new DomainException(ExceptionType.MEETING_ROOM_ALREADY_RESERVED);

		try {
			Reservation savedReservation = reservationRepository.save(reservation);
			List<ReservationSlot> slots = ReservationSlot.createBulk(savedReservation);
			reservationSlotRepository.saveAll(slots);

			return savedReservation;
		} catch (Exception ex) {
			log.error("회의실 예약 예외 발생 : {}", ex.getMessage());
			throw new DomainException(ExceptionType.RESERVATION_FAILED);
		}
	}

	@Transactional
	public Reservation cancel(long id, long userId) {
		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new DomainException(ExceptionType.NOT_FOUND_RESERVATION));

		reservationSlotRepository.deleteByReservationId(reservation.getId());
		reservation.cancel(userId);

		return reservation;
	}
}
