package com.wiseaiassignment.api.user.dto;

import com.wiseaiassignment.application.user.dto.CreateUserCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
		@NotBlank
		String name,

		@Email
		String email
) {
	public CreateUserCommand toCommand() {
		return new CreateUserCommand(name, email);
	}
}
