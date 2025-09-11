package com.wiseaiassignment.api.meetingroom.dto;

import com.wiseaiassignment.application.meetingroom.dto.MeetingRoomResult;

public record MeetingRoomResponse(
		long id,
		String name,
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
