package com.wiseaiassignment.domain.meetingroom.model;

public class MeetingRoomFactory {

	public static MeetingRoom create(
			long id,
			String name
	) {
		return new MeetingRoom(id, name, 10);
	}
}
