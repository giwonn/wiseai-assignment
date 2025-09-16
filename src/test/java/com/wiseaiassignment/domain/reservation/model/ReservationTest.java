package com.wiseaiassignment.domain.reservation.model;

import com.wiseaiassignment.domain.common.exception.DomainException;
import com.wiseaiassignment.domain.common.exception.ExceptionType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

	@Test
	void 회의실_예약을_생성하면_status가_RESERVED으로_생성된다() {
		// given & when
		Reservation reservation = ReservationFactory.create(
				1L,
				"주간회의",
				LocalDateTime.of(2025,1,1,13,0),
				LocalDateTime.of(2025,1,1,14,0)
		);

		// then
		assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.RESERVED);
	}

	@Test
	void 회의실_예약을_취소하면_status가_CANCELED로_변경된다() {
		// given
		Reservation reservation = ReservationFactory.create(
				1L,
				"주간회의",
				LocalDateTime.of(2025,1,1,13,0),
				LocalDateTime.of(2025,1,1,14,0)
		);

		// when
		reservation.cancel();

		// then
		assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CANCELED);
	}

	@Test
	void 이미_취소된_예약을_취소하려_하면_ALREADY_CANCELED_RESERVATION_예외가_발생한다() {
		// given
		Reservation reservation = ReservationFactory.create(
				1L,
				"주간회의",
				LocalDateTime.of(2025,1,1,13,0),
				LocalDateTime.of(2025,1,1,14,0)
		);
		reservation.cancel();

		// when & then
		assertThatThrownBy(reservation::cancel)
				.isInstanceOf(DomainException.class)
				.extracting(ex -> ((DomainException)ex).getType())
				.isEqualTo(ExceptionType.ALREADY_CANCELED_RESERVATION);
	}

	@Test
	void 취소된_예약을_변경하려_하면_ALREADY_CANCELED_RESERVATION_예외가_발생한다() {
		// given
		Reservation reservation = ReservationFactory.create(
				1L,
				"주간회의",
				LocalDateTime.of(2025,1,1,13,0),
				LocalDateTime.of(2025,1,1,14,0)
		);
		reservation.cancel();

		// when & then
		assertThatThrownBy(() -> reservation.changeReservation(
				2L,
				LocalDateTime.of(2025,1,1,15,0),
				LocalDateTime.of(2025,1,1,16,0)
		))
				.isInstanceOf(DomainException.class)
				.extracting(ex -> ((DomainException)ex).getType())
				.isEqualTo(ExceptionType.ALREADY_CANCELED_RESERVATION);
	}

	@Test
	void 성공() {
		// given
		Reservation reservation = ReservationFactory.create(
				1L,
				"주간회의",
				LocalDateTime.of(2025,1,1,13,0),
				LocalDateTime.of(2025,1,1,14,0)
		);

		// when
		reservation.changeReservation(
				2L,
				LocalDateTime.of(2025,1,1,15,0),
				LocalDateTime.of(2025,1,1,16,0)
		);

		// then
		assertThat(reservation.getMeetingRoomId()).isEqualTo(2L);
		assertThat(reservation.getStartTime()).isEqualTo(LocalDateTime.of(2025,1,1,15,0));
		assertThat(reservation.getEndTime()).isEqualTo(LocalDateTime.of(2025,1,1,16,0));
	}

}
