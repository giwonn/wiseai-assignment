package com.wiseaiassignment.domain.common.exception;

import lombok.Getter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionType {

	// MeetingRoom
	NOT_FOUND_MEETING_ROOM(HttpStatus.NOT_FOUND, "존재하지 않는 희의실입니다.", LogLevel.WARN),

	// Reservation
	NOT_FOUND_RESERVATION(HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다.", LogLevel.WARN),
	INVALID_RESERVATION_TIME_RANGE(HttpStatus.BAD_REQUEST, "예약 시작시간은 종료시간보다 과거여야 합니다.", LogLevel.WARN),
	RESERVATION_TIME_MUST_BE_HALF_HOUR(HttpStatus.BAD_REQUEST, "예약시간은 0분 혹은 30분이어야 합니다.", LogLevel.WARN),
	INVALID_RESERVATION_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 예약 요청입니다.", LogLevel.WARN),
	MEETING_ROOM_ALREADY_RESERVED(HttpStatus.CONFLICT, "이미 예약된 시간입니다.", LogLevel.WARN),
	RESERVATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "회의실 예약에 실패하였습니다.", LogLevel.ERROR),
	NOT_RESERVATION_HOST(HttpStatus.BAD_REQUEST, "예약 주최자가 아닙니다.", LogLevel.WARN),
	ALREADY_CANCELED_RESERVATION(HttpStatus.BAD_REQUEST, "이미 취소된 예약입니다.", LogLevel.WARN),

	// User
	NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다.", LogLevel.WARN);

	private final HttpStatus status;
	private final String message;
	private final LogLevel logLevel;

	ExceptionType(HttpStatus status, String message, LogLevel logLevel) {
		this.status = status;
		this.message = message;
		this.logLevel = logLevel;
	}

}
