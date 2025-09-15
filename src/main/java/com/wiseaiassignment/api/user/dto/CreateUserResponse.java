package com.wiseaiassignment.api.user.dto;

import com.wiseaiassignment.application.user.dto.CreateUserResult;

import java.time.Instant;

public record CreateUserResponse(
		long id,
		String name,
		String email,
		boolean active,
		Instant createdAt,
		Instant updatedAt
) {
	public static CreateUserResponse from(CreateUserResult result) {
		return new CreateUserResponse(
				result.id(),
				result.name(),
				result.email(),
				result.active(),
				result.createdAt(),
				result.updatedAt()
		);
	}
}
