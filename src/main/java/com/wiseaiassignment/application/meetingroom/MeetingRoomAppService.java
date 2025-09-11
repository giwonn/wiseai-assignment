package com.wiseaiassignment.application.meetingroom;

import com.wiseaiassignment.application.meetingroom.dto.MeetingRoomResult;
import com.wiseaiassignment.domain.meetingroom.MeetingRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MeetingRoomAppService {

	private final MeetingRoomService meetingRoomService;

	public List<MeetingRoomResult> findAllMeetingRooms() {
		return meetingRoomService.findAll().stream()
				.map(MeetingRoomResult::from)
				.toList();
	}
}
