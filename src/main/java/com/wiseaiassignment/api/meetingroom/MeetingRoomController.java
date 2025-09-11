package com.wiseaiassignment.api.meetingroom;

import com.wiseaiassignment.api.meetingroom.dto.MeetingRoomResponse;
import com.wiseaiassignment.api.ApiCustomResponse;
import com.wiseaiassignment.application.meetingroom.MeetingRoomAppService;
import com.wiseaiassignment.application.meetingroom.dto.MeetingRoomResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/meeting-rooms")
public class MeetingRoomController implements MeetingRoomApiSpec {

	private final MeetingRoomAppService meetingRoomAppService;

	@GetMapping(path = "")
	public ApiCustomResponse<List<MeetingRoomResponse>> getList() {
		List<MeetingRoomResult> results = meetingRoomAppService.findAllMeetingRooms();
		List<MeetingRoomResponse> responses = results.stream().map(MeetingRoomResponse::from).toList();
		return ApiCustomResponse.of(responses);
	}

}
