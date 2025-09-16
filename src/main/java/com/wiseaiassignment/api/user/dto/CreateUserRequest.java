package com.wiseaiassignment.api.user.dto;

import com.wiseaiassignment.application.user.dto.CreateUserCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "사용자 생성 요청")
public record CreateUserRequest(
		@Schema(description = "사용자 이름", example = "사용자1")
		@NotBlank
		String name,

		@Schema(description = "이메일 주소", example = "test@gmail.com")
		@Email
		String email
) {
	public CreateUserCommand toCommand() {
		return new CreateUserCommand(name, email);
	}
}
