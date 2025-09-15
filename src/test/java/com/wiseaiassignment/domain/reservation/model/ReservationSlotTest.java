package com.wiseaiassignment.domain.reservation.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationSlotTest {

	@Test
	void createBulk는_30분_단위의_시간_슬롯을_생성한다() {
		// given
		Reservation reservation = ReservationFactory.create(
				1L,
				"회의",
				LocalDateTime.of(2025, 1, 1, 10, 0),
				LocalDateTime.of(2025, 1, 1, 11, 0)
		);

		// when
		List<ReservationSlot> slots = ReservationSlot.createBulk(reservation);

		// then
		assertThat(slots).hasSize(2);
		assertThat(slots.get(0).getSlotTime()).isEqualTo(LocalDateTime.of(2025, 1, 1, 10, 0));
		assertThat(slots.get(1).getSlotTime()).isEqualTo(LocalDateTime.of(2025, 1, 1, 10, 30));
	}

}
