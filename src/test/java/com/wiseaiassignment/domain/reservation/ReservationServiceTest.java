package com.wiseaiassignment.domain.reservation;

import com.wiseaiassignment.domain.common.exception.DomainException;
import com.wiseaiassignment.domain.common.exception.ExceptionType;
import com.wiseaiassignment.domain.reservation.model.*;
import com.wiseaiassignment.domain.reservation.repository.ReservationAttendeeRepository;
import com.wiseaiassignment.domain.reservation.repository.ReservationRepository;
import com.wiseaiassignment.domain.reservation.repository.ReservationSlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

	@InjectMocks
	ReservationService reservationService;

	@Mock
	ReservationRepository reservationRepository;

	@Mock
	ReservationSlotRepository reservationSlotRepository;

	@Mock
	ReservationAttendeeRepository reservationAttendeeRepository;

	Reservation reservation;

	@BeforeEach
	void setUp() {
		reservation = ReservationFactory.create(
				1L,
				"주간회의",
				LocalDateTime.of(2025,1,1,10,0),
				LocalDateTime.of(2025,1,1,11,0)
		);
	}

	@Nested
	@DisplayName("회의실 예약 시도")
	class ReservationTest {

		@Test
		void 성공() {
			// given
			given(reservationRepository.save(any(Reservation.class))).willReturn(reservation);
			given(reservationSlotRepository.saveAll(anyList())).willReturn(null);
			given(reservationAttendeeRepository.saveAll(anyList())).willReturn(List.of());

			// when
			ReservationDetail result = reservationService.reserve(reservation, List.of());

			// then
			assertThat(result).isNotNull();
			verify(reservationRepository).save(any(Reservation.class));
			verify(reservationSlotRepository).saveAll(anyList());
		}
	}

	@Nested
	@DisplayName("회의실 예약 취소")
	class CancelReservationTest {

		@Test
		void 성공() {
			// given
			long reservationId = 1L;
			long userId = 1L;
			Reservation spyReservation = spy(reservation);
			given(reservationRepository.findByIdWithAttendees(anyLong())).willReturn(Optional.of(spyReservation));
			given(reservationSlotRepository.deleteByReservationId(anyLong())).willReturn(1L);

			// when
			ReservationDetail result = reservationService.cancel(reservationId, userId);

			// then
			assertThat(result).isNotNull();
			assertThat(result.status()).isEqualTo(ReservationStatus.CANCELED);
		}

		@Test
		void 예약이_존재하지_않으면_NOT_FOUND_RESERVATION_예외_발생() {
			// given
			given(reservationRepository.findByIdWithAttendees(any())).willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> reservationService.cancel(1L, 1L))
					.isInstanceOf(DomainException.class)
					.extracting(ex -> ((DomainException) ex).getType())
					.isEqualTo(ExceptionType.NOT_FOUND_RESERVATION);
		}

	}
}
