package com.wiseaiassignment.api.reservation.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CreateReservationRequest(
		@NotNull Long roomId,
		@NotNull String title,
		@NotNull LocalDateTime startTime,
		@NotNull LocalDateTime endTime,
		@NotNull Long userId,
		List<String> attendeeEmails
) {

}
