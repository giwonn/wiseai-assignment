package com.wiseaiassignment.api.meetingroom.dto;

import java.time.LocalDateTime;
import java.util.List;

public record MeetingRoomScheduleResponse(
		long roomId,
		int capacity,
		List<MeetingRoomTimeline> timelines
) {

	public record MeetingRoomTimeline(
			LocalDateTime start,
			LocalDateTime end,
			boolean isReserved,
			Long reservationId
	) {}
}
