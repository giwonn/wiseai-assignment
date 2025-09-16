package com.wiseaiassignment.api.reservation.dto;

import com.wiseaiassignment.application.reservation.dto.ChangeReservationCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "회의실 예약 생성 요청")
public record ChangeReservationRequest(
		@Schema(description = "회의실 ID", example = "1")
		@NotNull Long roomId,

		@Schema(description = "예약 주최자 ID", example = "1")
		@NotNull Long userId,

		@Schema(description = "예약 시작 시간", example = "2025-09-15T09:00:00")
		@NotNull LocalDateTime startTime,

		@Schema(description = "예약 종료 시간", example = "2025-09-15T10:00:00")
		@NotNull LocalDateTime endTime
) {

	public ChangeReservationCommand toChangeCommand(long reservationId) {
		return new ChangeReservationCommand(
				reservationId,
				roomId,
				userId,
				startTime,
				endTime
		);
	}

}
