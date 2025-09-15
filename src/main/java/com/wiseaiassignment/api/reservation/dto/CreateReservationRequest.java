package com.wiseaiassignment.api.reservation.dto;

import com.wiseaiassignment.application.reservation.dto.CreateReservationCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CreateReservationRequest(
		@NotNull Long roomId,
		@NotBlank String title,
		@NotNull LocalDateTime startTime,
		@NotNull LocalDateTime endTime,
		@NotNull Long userId,
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
