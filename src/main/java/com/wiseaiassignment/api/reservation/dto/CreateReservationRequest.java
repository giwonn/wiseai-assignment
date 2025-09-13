package com.wiseaiassignment.api.reservation.dto;

import com.wiseaiassignment.application.reservation.dto.CreateReservationCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CreateReservationRequest(
		@NotNull Long roomId,
		@NotNull String title,
		@NotNull LocalDateTime startTime,
		@NotNull LocalDateTime endTime,
		@NotNull Long userId,
		List<@Email String> attendeeEmails
) {

	public CreateReservationCommand toCreateCommand() {
		return new CreateReservationCommand(
				title,
				userId,
				roomId,
				startTime,
				endTime,
				attendeeEmails
		);
	}

}
