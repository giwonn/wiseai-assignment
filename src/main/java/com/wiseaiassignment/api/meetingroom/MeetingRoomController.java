package com.wiseaiassignment.api.meetingroom;

import com.wiseaiassignment.api.meetingroom.dto.CreateMeetingRoomRequest;
import com.wiseaiassignment.api.meetingroom.dto.MeetingRoomResponse;
import com.wiseaiassignment.api.ApiCustomResponse;
import com.wiseaiassignment.application.meetingroom.MeetingRoomAppService;
import com.wiseaiassignment.application.meetingroom.dto.MeetingRoomResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/meeting-rooms")
public class MeetingRoomController implements MeetingRoomApiSpec {

	private final MeetingRoomAppService meetingRoomAppService;

	@Override
	@PostMapping(path = "")
	public ApiCustomResponse<MeetingRoomResponse> create(@Valid @RequestBody CreateMeetingRoomRequest request) {
		MeetingRoomResult result = meetingRoomAppService.createMeetingRoom(request.toCommand());
		return ApiCustomResponse.of(MeetingRoomResponse.from(result));
	}

	@Override
	@GetMapping(path = "")
	public ApiCustomResponse<List<MeetingRoomResponse>> getList() {
		List<MeetingRoomResult> results = meetingRoomAppService.findAllMeetingRooms();
		List<MeetingRoomResponse> responses = results.stream().map(MeetingRoomResponse::from).toList();
		return ApiCustomResponse.of(responses);
	}
}
