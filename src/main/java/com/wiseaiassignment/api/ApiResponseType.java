package com.wiseaiassignment.api;

public enum ApiResponseType {
	SUCCESS("성공"),
	FAIL("실패");

	private final String description;

	ApiResponseType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
