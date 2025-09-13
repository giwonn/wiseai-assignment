package com.wiseaiassignment.domain.reservation.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationTest {

	@Test
	void 회의실_예약을_생성하면_status가_RESERVED으로_생성된다() {
		// given & when
		Reservation reservation = Reservation.create(
				"주간회의",
				1L,
				1L,
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
				1L,
				LocalDateTime.of(2025,1,1,13,0),
				LocalDateTime.of(2025,1,1,14,0),
				List.of()
		);

		// when
		reservation.cancel();

		// then
		assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CANCELED);
	}

}
