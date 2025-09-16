package com.wiseaiassignment.api.meetingroom.dto;

import com.wiseaiassignment.application.meetingroom.dto.CreateMeetingRoomCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "회의실 생성 요청")
public record CreateMeetingRoomRequest(
		@Schema(description = "회의실 이름", example = "회의실1")
		@NotBlank String name,

		@Schema(description = "수용 인원", example = "10")
		@NotNull @Min(1) int capacity
) {
	public CreateMeetingRoomCommand toCommand() {
		return new CreateMeetingRoomCommand(
				this.name,
				this.capacity
		);
	}
}
