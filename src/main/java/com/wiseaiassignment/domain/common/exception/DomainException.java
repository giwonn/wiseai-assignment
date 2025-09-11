package com.wiseaiassignment.domain.common.exception;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

	private final ExceptionType type;

	public DomainException(ExceptionType type) {
		super(type.getMessage());
		this.type = type;
	}

}
