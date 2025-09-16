package com.wiseaiassignment.api.meetingroom.dto;

import com.wiseaiassignment.application.meetingroom.dto.MeetingRoomResult;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회의실 정보 응답")
public record MeetingRoomResponse(
		@Schema(description = "회의실 ID", example = "1")
		long id,

		@Schema(description = "회의실 이름", example = "회의실1")
		String name,

		@Schema(description = "수용 인원", example = "10")
		int capacity
) {

	public static MeetingRoomResponse from(MeetingRoomResult result) {
		return new MeetingRoomResponse(
				result.meetingRoomId(),
				result.meetingRoomName(),
				result.capacity()
		);
	}

}
