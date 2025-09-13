package com.wiseaiassignment.domain.reservation.model;

import com.wiseaiassignment.domain.common.exception.DomainException;
import com.wiseaiassignment.domain.common.exception.ExceptionType;
import com.wiseaiassignment.domain.common.lib.DateLib;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class TimeRange {

	public static final int MINIMUM_RESERVATION_MINUTES = 30;

	@Column(nullable = false, columnDefinition = "DATETIME(0)")
	private LocalDateTime startTime;

	@Column(nullable = false, columnDefinition = "DATETIME(0)")
	private LocalDateTime endTime;

	static TimeRange of(LocalDateTime start, LocalDateTime end) {
		start = DateLib.truncateToMinute(start);
		end = DateLib.truncateToMinute(end);

		validateTimeRange(start, end);
		validateHalfTime(start, end);

		return new TimeRange(start, end);
	}

	private static void validateTimeRange(LocalDateTime start, LocalDateTime end) {
		if (!start.isBefore(end)) {
			throw new DomainException(ExceptionType.INVALID_RESERVATION_TIME_RANGE);
		}
	}

	private static void validateHalfTime(LocalDateTime start, LocalDateTime end) {
		if (!(isHalfHour(start) && isHalfHour(end))) {
			throw new DomainException(ExceptionType.RESERVATION_TIME_MUST_BE_HALF_HOUR);
		}
	}

	private static boolean isHalfHour(LocalDateTime time) {
		return time.getMinute() % MINIMUM_RESERVATION_MINUTES == 0;
	}

}
