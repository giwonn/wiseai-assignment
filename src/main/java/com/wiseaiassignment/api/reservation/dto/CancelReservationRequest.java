package com.wiseaiassignment.api.reservation.dto;

import jakarta.validation.constraints.NotNull;

public record CancelReservationRequest(
		@NotNull Long userId
) {

}
