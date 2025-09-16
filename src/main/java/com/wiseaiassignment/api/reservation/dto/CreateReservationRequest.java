package com.wiseaiassignment.api.reservation.dto;

import com.wiseaiassignment.application.reservation.dto.CreateReservationCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "회의실 예약 생성 요청")
public record CreateReservationRequest(
		@Schema(description = "회의실 ID", example = "1")
		@NotNull Long roomId,

		@Schema(description = "예약 제목", example = "주간회의")
		@NotBlank String title,

		@Schema(description = "예약 시작 시간", example = "2025-09-15T09:00:00")
		@NotNull LocalDateTime startTime,

		@Schema(description = "예약 종료 시간", example = "2025-09-15T10:00:00")
		@NotNull LocalDateTime endTime,

		@Schema(description = "예약자 사용자 ID", example = "1")
		@NotNull Long userId,

		@Schema(description = "참여자 사용자 ID 목록", example = "[]")
		List<Long> attendeeUserIds
) {

	public CreateReservationCommand toCreateCommand() {
		return new CreateReservationCommand(
				title,
				userId,
				roomId,
				startTime,
				endTime,
				attendeeUserIds == null ? List.of() : attendeeUserIds
		);
	}

}
