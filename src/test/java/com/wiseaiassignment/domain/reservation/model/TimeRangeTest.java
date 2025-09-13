package com.wiseaiassignment.domain.reservation.model;

import com.wiseaiassignment.domain.common.exception.DomainException;
import com.wiseaiassignment.domain.common.exception.ExceptionType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeRangeTest {

	@Nested
	class 예약_시작시간은_종료시간보다_과거여야_한다 {

		@Test
		void 시작시간이_종료시간보다_과거면_TimeRange_객체가_성공적으로_생성된다() {
			// given
			LocalDateTime start = LocalDateTime.of(2025,1,1,14,0);
			LocalDateTime end = LocalDateTime.of(2025,1,1,14,30);

			// when & then
			TimeRange.of(start, end);
		}

		@Test
		void 시작시간이_종료시간과_같으면_INVALID_RESERVATION_TIME_RANGE_예외가_발생한다() {
			// given
			LocalDateTime start = LocalDateTime.of(2025,1,1,14,0);
			LocalDateTime end = LocalDateTime.of(2025,1,1,14,0);

			// when & then
			assertThatThrownBy(() -> TimeRange.of(start, end))
					.isInstanceOf(DomainException.class)
					.extracting(ex -> ((DomainException) ex).getType())
					.isEqualTo(ExceptionType.INVALID_RESERVATION_TIME_RANGE);
		}

		@Test
		void 시작시간이_종료시간보다_미래면_INVALID_RESERVATION_TIME_RANGE_예외가_발생한다() {
			// given
			LocalDateTime start = LocalDateTime.of(2025,1,1,14,1);
			LocalDateTime end = LocalDateTime.of(2025,1,1,14,0);

			// when & then
			assertThatThrownBy(() -> TimeRange.of(start, end))
					.isInstanceOf(DomainException.class)
					.extracting(ex -> ((DomainException) ex).getType())
					.isEqualTo(ExceptionType.INVALID_RESERVATION_TIME_RANGE);
		}
	}

	@Nested
	class 예약시간은_0분_혹은_30분이어야_한다 {

		@Test
		void 시작시간과_종료시간이_0분이면_TimeRange_객체가_성공적으로_생성된다() {
			// given
			LocalDateTime start = LocalDateTime.of(2025, 1, 1, 14, 0);
			LocalDateTime end = LocalDateTime.of(2025, 1, 1, 15, 0);

			// when & then
			TimeRange.of(start, end);
		}

		@Test
		void 시작시간과_종료시간이_30분이면_TimeRange_객체가_성공적으로_생성된다() {
			// given
			LocalDateTime start = LocalDateTime.of(2025, 1, 1, 14, 30);
			LocalDateTime end = LocalDateTime.of(2025, 1, 1, 15, 30);

			// when & then
			TimeRange.of(start, end);
		}

		@Test
		void 시작시간이_15분이면_RESERVATION_TIME_MUST_BE_HALF_HOUR_예외가_발생한다() {
			// given
			LocalDateTime start = LocalDateTime.of(2025, 1, 1, 14, 15);
			LocalDateTime end = LocalDateTime.of(2025, 1, 1, 15, 0);

			// when & then
			assertThatThrownBy(() -> TimeRange.of(start, end))
					.isInstanceOf(DomainException.class)
					.extracting(ex -> ((DomainException) ex).getType())
					.isEqualTo(ExceptionType.RESERVATION_TIME_MUST_BE_HALF_HOUR);
		}

		@Test
		void 종료시간이_15분이면_RESERVATION_TIME_MUST_BE_HALF_HOUR_예외가_발생한다() {
			// given
			LocalDateTime start = LocalDateTime.of(2025, 1, 1, 14, 0);
			LocalDateTime end = LocalDateTime.of(2025, 1, 1, 15, 15);

			// when & then
			assertThatThrownBy(() -> TimeRange.of(start, end))
					.isInstanceOf(DomainException.class)
					.extracting(ex -> ((DomainException) ex).getType())
					.isEqualTo(ExceptionType.RESERVATION_TIME_MUST_BE_HALF_HOUR);
		}
	}

}
