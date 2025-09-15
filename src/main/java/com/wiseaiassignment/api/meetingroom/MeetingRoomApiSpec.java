package com.wiseaiassignment.api.meetingroom;

import com.wiseaiassignment.api.ApiCustomResponse;
import com.wiseaiassignment.api.meetingroom.dto.CreateMeetingRoomRequest;
import com.wiseaiassignment.api.meetingroom.dto.MeetingRoomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "MeetingRooms", description = "회의실 API")
public interface MeetingRoomApiSpec {

	@Operation(summary = "회의실 생성", description = "새로운 회의실을 생성합니다.")
	@ApiResponse(responseCode = "200", description = "회의실 생성 성공")
	ApiCustomResponse<MeetingRoomResponse> create(CreateMeetingRoomRequest request);

	@Operation(summary = "회의실 목록 조회", description = "사용 가능한 회의실 목록을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "회의실 목록 조회 성공")
	ApiCustomResponse<List<MeetingRoomResponse>> getList();

}
