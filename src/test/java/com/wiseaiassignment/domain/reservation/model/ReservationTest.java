package com.wiseaiassignment.domain.reservation.model;

import com.wiseaiassignment.domain.common.exception.DomainException;
import com.wiseaiassignment.domain.common.exception.ExceptionType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

	@Test
	void 회의실_예약을_생성하면_status가_RESERVED으로_생성된다() {
		// given & when
		Reservation reservation = Reservation.create(
				"주간회의",
				1L,
				"test1@email.com",
				1L,
				"회의실1",
				LocalDateTime.of(2025,1,1,13,0),
				LocalDateTime.of(2025,1,1,14,0),
				List.of()
		);

		// then
		assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.RESERVED);
	}

	@Test
	void 회의실_예약을_취소하면_status가_CANCELED로_변경된다() {
		// given
		Reservation reservation = Reservation.create(
				"주간회의",
				1L,
				"test1@email.com",
				1L,
				"회의실1",
				LocalDateTime.of(2025,1,1,13,0),
				LocalDateTime.of(2025,1,1,14,0),
				List.of()
		);

		// when
		reservation.cancel(reservation.getUserId());

		// then
		assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CANCELED);
	}

	@Test
	void 다른_유저가_예약을_취소하면_NOT_RESERVATION_HOST_예외가_발생한다() {
		// given
		Reservation reservation = Reservation.create(
				"주간회의",
				1L,
				"test1@email.com",
				1L,
				"회의실1",
				LocalDateTime.of(2025,1,1,13,0),
				LocalDateTime.of(2025,1,1,14,0),
				List.of()
		);

		// when & then
		assertThatThrownBy(() -> reservation.cancel(2L))
				.isInstanceOf(DomainException.class)
				.extracting(ex -> ((DomainException)ex).getType())
				.isEqualTo(ExceptionType.NOT_RESERVATION_HOST);

	}

	@Test
	void 이미_취소된_예약을_취소하려_하면_ALREADY_CANCELED_RESERVATION_예외가_발생한다() {
		// given
		Reservation reservation = Reservation.create(
				"주간회의",
				1L,
				"test1@email.com",
				1L,
				"회의실1",
				LocalDateTime.of(2025,1,1,13,0),
				LocalDateTime.of(2025,1,1,14,0),
				List.of()
		);
		reservation.cancel(reservation.getUserId());

		// when & then
		assertThatThrownBy(() -> reservation.cancel(reservation.getUserId()))
				.isInstanceOf(DomainException.class)
				.extracting(ex -> ((DomainException)ex).getType())
				.isEqualTo(ExceptionType.ALREADY_CANCELED_RESERVATION);
	}

}
