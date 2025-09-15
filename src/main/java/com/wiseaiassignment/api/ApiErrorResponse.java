package com.wiseaiassignment.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(
		ApiResponseType result,
		String errorMessage
) {
	public static ApiErrorResponse of(String errorMessage) {
		return new ApiErrorResponse(ApiResponseType.FAIL, errorMessage);
	}
}
