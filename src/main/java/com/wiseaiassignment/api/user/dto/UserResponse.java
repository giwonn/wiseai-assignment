package com.wiseaiassignment.api.user.dto;

import com.wiseaiassignment.application.user.dto.UserResult;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "유저 정보 응답")
public record UserResponse(
		@Schema(description = "유저 ID", example = "1")
		long id,

		@Schema(description = "유저 이름", example = "사용자1")
		String name,

		@Schema(description = "이메일", example = "test1@gmail.com")
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
