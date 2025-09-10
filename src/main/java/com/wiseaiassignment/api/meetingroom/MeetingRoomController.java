package com.wiseaiassignment.api.meetingroom;

import com.wiseaiassignment.api.meetingroom.dto.MeetingRoomResponse;
import com.wiseaiassignment.api.meetingroom.dto.MeetingRoomScheduleResponse;
import com.wiseaiassignment.api.ApiCustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/meeting-rooms")
public class MeetingRoomController implements MeetingRoomApiSpec {

	@GetMapping(path = "")
	public ApiCustomResponse<List<MeetingRoomResponse>> getList() {
		throw new Error("구현 예정");
	}

}
