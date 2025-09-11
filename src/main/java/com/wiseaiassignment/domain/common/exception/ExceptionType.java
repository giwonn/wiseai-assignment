package com.wiseaiassignment.domain.common.exception;

import lombok.Getter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionType {

	// MeetingRoom
	NOT_FOUND_MEETING_ROOM(HttpStatus.NOT_FOUND, "존재하지 않는 희의실입니다.", LogLevel.WARN),

	private final HttpStatus status;
	private final String message;
	private final LogLevel logLevel;

	ExceptionType(HttpStatus status, String message, LogLevel logLevel) {
		this.status = status;
		this.message = message;
		this.logLevel = logLevel;
	}

}
