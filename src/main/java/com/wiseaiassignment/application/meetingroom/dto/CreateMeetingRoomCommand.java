package com.wiseaiassignment.application.meetingroom.dto;

import com.wiseaiassignment.domain.meetingroom.model.MeetingRoom;

public record CreateMeetingRoomCommand(
		String name,
		int capacity
) {
	public MeetingRoom toEntity() {
		return MeetingRoom.create(name, capacity);
	}
}
