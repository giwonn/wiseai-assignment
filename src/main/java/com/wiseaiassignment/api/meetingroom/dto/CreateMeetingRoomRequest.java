package com.wiseaiassignment.api.meetingroom.dto;

import com.wiseaiassignment.application.meetingroom.dto.CreateMeetingRoomCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMeetingRoomRequest(
		@NotBlank String name,
		@NotNull @Min(1) int capacity
) {
	public CreateMeetingRoomCommand toCommand() {
		return new CreateMeetingRoomCommand(
				this.name,
				this.capacity
		);
	}
}
