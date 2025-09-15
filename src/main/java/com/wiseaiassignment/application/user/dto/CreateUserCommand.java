package com.wiseaiassignment.application.user.dto;

import com.wiseaiassignment.domain.user.model.User;

public record CreateUserCommand(
		String name,
		String email
) {
	public User toEntity() {
		return User.create(name, email);
	}
}
