package com.wiseaiassignment.api.user.dto;

import com.wiseaiassignment.application.user.dto.CreateUserResult;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "사용자 생성 응답")
public record CreateUserResponse(
		@Schema(description = "사용자 ID", example = "1")
		long id,

		@Schema(description = "사용자 이름", example = "사용자1")
		String name,

		@Schema(description = "이메일 주소", example = "test@gmail.com")
		String email,

		@Schema(description = "활성 상태", example = "true")
		boolean active,

		@Schema(description = "생성 시간", example = "2025-09-15T09:00:00Z")
		Instant createdAt,

		@Schema(description = "수정 시간", example = "2025-09-15T09:00:00Z")
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
