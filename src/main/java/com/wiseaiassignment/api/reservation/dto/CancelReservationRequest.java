package com.wiseaiassignment.api.reservation.dto;

import com.wiseaiassignment.application.reservation.dto.CancelReservationCommand;
import jakarta.validation.constraints.NotNull;

public record CancelReservationRequest(
		@NotNull Long userId
) {
	public CancelReservationCommand toCancelCommand(long reservationId) {
		return new CancelReservationCommand(reservationId, userId);
	}
}
