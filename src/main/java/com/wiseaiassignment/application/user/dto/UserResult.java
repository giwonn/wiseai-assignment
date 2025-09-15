package com.wiseaiassignment.application.user.dto;

import com.wiseaiassignment.domain.user.model.User;

public record UserResult(
		long id,
		String name,
		String email,
		boolean active
) {
	public static UserResult from(User user) {
		return new UserResult(
				user.getId(),
				user.getName(),
				user.getEmail(),
				user.isActive()
		);
	}
}
