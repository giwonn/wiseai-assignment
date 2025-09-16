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
			given(reservationRepository.findByIdWithAttendees(anyLong())).willReturn(Optional.of(reservation));
			given(reservationSlotRepository.deleteByReservationId(anyLong())).willReturn(1L);

			// when
			ReservationDetail result = reservationService.cancel(reservationId, userId);

			// then
			assertThat(result).isNotNull();
			assertThat(result.status()).isEqualTo(ReservationStatus.CANCELED);
		}

		@Test
		void 주최자가_아닌_유저가_예약을_취소하면_NOT_RESERVATION_HOST_예외가_발생한다() {
			// given
			given(reservationRepository.findByIdWithAttendees(any())).willReturn(Optional.of(reservation));

			// when & then
			assertThatThrownBy(() -> reservationService.cancel(reservation.getId(), reservation.getOrganizerId()+1L))
					.isInstanceOf(DomainException.class)
					.extracting(ex -> ((DomainException) ex).getType())
					.isEqualTo(ExceptionType.NOT_RESERVATION_HOST);
		}
	}

	@Nested
	@DisplayName("회의실 예약 시간 변경")
	class ChangeReservationTest {

		@Test
		void 변경하려는_예약이_존재하지_않으면_NOT_FOUND_RESERVATION_예외가_발생한다() {
			// given
			given(reservationRepository.findByIdWithAttendees(anyLong())).willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() ->
					reservationService.changeTime(
						1L,
						1L,
						1L,
						LocalDateTime.of(2025,1,1,12,0),
						LocalDateTime.of(2025,1,1,13,0)
					)
			)
					.isInstanceOf(DomainException.class)
					.extracting(ex -> ((DomainException) ex).getType())
					.isEqualTo(ExceptionType.NOT_FOUND_RESERVATION);
		}

		@Test
		void 주최자가_아닌_유저가_예약시간을_변경하면_NOT_RESERVATION_HOST_예외가_발생한다() {
			// given
			given(reservationRepository.findByIdWithAttendees(anyLong())).willReturn(Optional.of(reservation));

			// when & then
			assertThatThrownBy(() -> reservationService.changeTime(
						reservation.getId(),
						reservation.getOrganizerId()+1L, 1L,
						LocalDateTime.of(2025,1,1,12,0),
						LocalDateTime.of(2025,1,1,13,0)
					)
			)
					.isInstanceOf(DomainException.class)
					.extracting(ex -> ((DomainException) ex).getType())
					.isEqualTo(ExceptionType.NOT_RESERVATION_HOST);
		}

		@Test
		void 회의실이_이미_예약되어있으면_MEETING_ROOM_ALREADY_RESERVED_예외가_발생한다() {
			// given
			given(reservationRepository.findByIdWithAttendees(anyLong())).willReturn(Optional.of(reservation));
			given(reservationRepository.findConflictReservations(anyLong(), any(), any()))
					.willReturn(
							List.of(
									ReservationFactory.create(
											2L,
											"다른회의",
											LocalDateTime.of(2025,1,1,12,0),
											LocalDateTime.of(2025,1,1,13,0)
									)
							)
					);

			// when & then
			assertThatThrownBy(() ->
					reservationService.changeTime(
							reservation.getId(),
							reservation.getOrganizerId(), 1L,
							LocalDateTime.of(2025,1,1,12,0),
							LocalDateTime.of(2025,1,1,13,0)
					)
			)
					.isInstanceOf(DomainException.class)
					.extracting(ex -> ((DomainException) ex).getType())
					.isEqualTo(ExceptionType.MEETING_ROOM_ALREADY_RESERVED);
		}

		@Test
		void 성공() {
			// given
			given(reservationRepository.findByIdWithAttendees(anyLong())).willReturn(Optional.of(reservation));
			given(reservationSlotRepository.deleteByReservationId(anyLong())).willReturn(1L);
			given(reservationRepository.findConflictReservations(anyLong(), any(), any())).willReturn(List.of());
			given(reservationSlotRepository.saveAll(anyList())).willReturn(null);

			// when
			ReservationDetail result = reservationService.changeTime(reservation.getId(), reservation.getOrganizerId(), 1L,
					LocalDateTime.of(2025,1,1,12,0),
					LocalDateTime.of(2025,1,1,13,0));

			// then
			assertThat(result).isNotNull();
			assertThat(result.startTime()).isEqualTo(LocalDateTime.of(2025,1,1,12,0));
			assertThat(result.endTime()).isEqualTo(LocalDateTime.of(2025,1,1,13,0));
		}

	}
}
