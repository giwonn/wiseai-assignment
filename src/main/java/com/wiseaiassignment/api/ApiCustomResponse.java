package com.wiseaiassignment.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiCustomResponse<T>(
		ApiResponseType result,
		T data
) {

	public static <T> ApiCustomResponse<T> of(T data) {
		return new ApiCustomResponse<>(ApiResponseType.SUCCESS, data);
	}

}
