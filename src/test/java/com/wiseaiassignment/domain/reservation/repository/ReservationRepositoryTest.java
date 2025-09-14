package com.wiseaiassignment.domain.reservation.repository;

import com.wiseaiassignment.domain.reservation.model.Reservation;
import com.wiseaiassignment.helper.annotation.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class ReservationRepositoryTest {

	@Autowired
	ReservationRepository reservationRepository;

	@Nested
	@DisplayName("같은 회의실의 겹치는 예약시간 조회")
	class findConflictReservations {

		@Test
		void 예약시작시간에_종료되는_회의는_조회되지_않는다() {
			// given
			Reservation reservation = Reservation.create(
					"주간회의",
					1L,
					"test@email.com",
					1L,
					"회의실1",
					LocalDateTime.of(2024, 1, 1, 14, 0), // 14:00 ~ 15:00
					LocalDateTime.of(2024, 1, 1, 15, 0),
					List.of()
			);
			reservationRepository.save(reservation);

			// when
			List<Reservation> result = reservationRepository.findConflictReservations(
					1L,
					LocalDateTime.of(2024, 1, 1, 15, 0), // 15:00 ~ 16:00
					LocalDateTime.of(2024, 1, 1, 16, 0)
			);

			// then
			assertThat(result).isEmpty();
		}

		@Test
		void 예약시간이_겹치면_조회된다() {
			// given
			Reservation reservation = Reservation.create(
					"주간회의",
					1L,
					"test@email.com",
					1L,
					"회의실1",
					LocalDateTime.of(2024, 1, 1, 14, 0), // 14:00 ~ 15:00
					LocalDateTime.of(2024, 1, 1, 15, 0),
					List.of()
			);
			reservationRepository.save(reservation);

			// when
			List<Reservation> result = reservationRepository.findConflictReservations(
					1L,
					LocalDateTime.of(2024, 1, 1, 14, 30), // 14:30 ~ 15:30
					LocalDateTime.of(2024, 1, 1, 15, 30)
			);

			// then
			assertThat(result).hasSize(1);
		}

		@Test
		void 예약종료시간에_시작하는_회의는_겹치지_않는다() {
			// given
			Reservation reservation = Reservation.create(
					"주간회의",
					1L,
					"test@email.com",
					1L,
					"회의실1",
					LocalDateTime.of(2024, 1, 1, 14, 0), // 14:00 ~ 15:00
					LocalDateTime.of(2024, 1, 1, 15, 0),
					List.of()
			);
			reservationRepository.save(reservation);

			// when
			List<Reservation> result = reservationRepository.findConflictReservations(
					1L,
					LocalDateTime.of(2024, 1, 1, 13, 0), // 13:00 ~ 14:00
					LocalDateTime.of(2024, 1, 1, 14, 0)
			);

			// then
			assertThat(result).isEmpty();
		}

		@Test
		void 다른_회의실의_예약은_겹치지_않는다() {
			// given
			Reservation reservation = Reservation.create(
					"주간회의",
					1L,

					"test@email.com",
					1L,
					"회의실1",
					LocalDateTime.of(2024, 1, 1, 14, 0), // 14:00 ~ 15:00
					LocalDateTime.of(2024, 1, 1, 15, 0),
					List.of()
			);
			reservationRepository.save(reservation);

			// when
			List<Reservation> result = reservationRepository.findConflictReservations(
					2L, // 다른 회의실
					LocalDateTime.of(2024, 1, 1, 14, 30), // 14:30 ~ 15:30
					LocalDateTime.of(2024, 1, 1, 15, 30)
			);

			// then
			assertThat(result).isEmpty();
		}
	}


}
