package com.wiseaiassignment.application.meetingroom.dto;

import com.wiseaiassignment.domain.meetingroom.model.MeetingRoom;

public record MeetingRoomResult(
		long meetingRoomId,
		String meetingRoomName,
		int capacity
) {
	public static MeetingRoomResult from(MeetingRoom meetingRoom) {
		return new MeetingRoomResult(
				meetingRoom.getId(),
				meetingRoom.getName(),
				meetingRoom.getCapacity()
		);
	}
}
