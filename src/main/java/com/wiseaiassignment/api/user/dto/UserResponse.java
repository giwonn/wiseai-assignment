package com.wiseaiassignment.api.user.dto;

import com.wiseaiassignment.application.user.dto.UserResult;

import java.time.Instant;

public record UserResponse(
		long id,
		String name,
		String email
) {
	public static UserResponse from(UserResult result) {
		return new UserResponse(
				result.id(),
				result.name(),
				result.email()
		);
	}
}
