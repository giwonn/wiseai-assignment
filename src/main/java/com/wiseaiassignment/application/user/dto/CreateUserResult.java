package com.wiseaiassignment.application.user.dto;

import com.wiseaiassignment.domain.user.model.User;

import java.time.Instant;

public record CreateUserResult(
		long id,
		String name,
		String email,
		boolean active,
		Instant createdAt,
		Instant updatedAt
) {
	public static CreateUserResult from(User user) {
		return new CreateUserResult(
				user.getId(),
				user.getName(),
				user.getEmail(),
				user.isActive(),
				user.getCreatedAt(),
				user.getUpdatedAt()
		);
	}
}
