package com.wiseaiassignment.api.reservation.dto;

import com.wiseaiassignment.application.reservation.dto.CancelReservationCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "회의실 예약 취소 요청")
public record CancelReservationRequest(
		@Schema(description = "유저 ID", example = "1")
		@NotNull Long userId
) {
	public CancelReservationCommand toCancelCommand(long reservationId) {
		return new CancelReservationCommand(reservationId, userId);
	}
}
