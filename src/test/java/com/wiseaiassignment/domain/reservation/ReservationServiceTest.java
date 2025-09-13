package com.wiseaiassignment.domain.reservation;

import com.wiseaiassignment.domain.common.exception.DomainException;
import com.wiseaiassignment.domain.common.exception.ExceptionType;
import com.wiseaiassignment.domain.reservation.model.Reservation;
import com.wiseaiassignment.domain.reservation.model.ReservationFactory;
import com.wiseaiassignment.domain.reservation.repository.ReservationRepository;
import com.wiseaiassignment.domain.reservation.repository.ReservationSlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

	@InjectMocks
	ReservationService reservationService;

	@Mock
	ReservationRepository reservationRepository;

	@Mock
	ReservationSlotRepository reservationSlotRepository;

	Reservation reservation;

	@BeforeEach
	void setUp() {
		reservation = ReservationFactory.create(
				1L,
				"주간회의",
				1L,
				1L,
				LocalDateTime.of(2024, 1, 1, 10, 0),
				LocalDateTime.of(2024, 1, 1, 11, 0),
				List.of("test1@email.com", "test2@email.com")
		);
	}

	@Nested
	class 예약_시도 {

		@Test
		void 성공() {
			// given
			given(reservationRepository.findConflictReservations(any(), any(), any())).willReturn(List.of());
			given(reservationRepository.save(any(Reservation.class))).willReturn(reservation);
			given(reservationSlotRepository.saveAll(anyList())).willReturn(null);

			// when
			Reservation result = reservationService.reserve(reservation);

			// then
			assertThat(result).isNotNull();
			verify(reservationRepository).findConflictReservations(any(), any(), any());
			verify(reservationRepository).save(any(Reservation.class));
			verify(reservationSlotRepository).saveAll(anyList());
		}

		@Test
		void 예약시간이_겹치면_MEETING_ROOM_ALREADY_RESERVED_예외_발생() {
			// given
			given(reservationRepository.findConflictReservations(any(), any(), any())).willReturn(List.of(reservation));

			// when & then
			assertThatThrownBy(() -> reservationService.reserve(reservation))
					.isInstanceOf(DomainException.class)
					.extracting(ex -> ((DomainException) ex).getType())
					.isEqualTo(ExceptionType.MEETING_ROOM_ALREADY_RESERVED);
		}

		@Test
		void 일반적인_예외는_RESERVATION_FAILED_예외로_변환된다() {
			// given
			given(reservationRepository.save(any(Reservation.class))).willReturn(reservation);
			willThrow(new RuntimeException("Connection timeout"))
					.given(reservationSlotRepository).saveAll(anyList());

			// when & then
			assertThatThrownBy(() -> reservationService.reserve(reservation))
					.isInstanceOf(DomainException.class)
					.extracting(ex -> ((DomainException) ex).getType())
					.isEqualTo(ExceptionType.RESERVATION_FAILED);
		}
	}
}
