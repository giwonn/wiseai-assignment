package com.wiseaiassignment.api.reservation;

import com.wiseaiassignment.api.meetingroom.dto.MeetingRoomScheduleResponse;
import com.wiseaiassignment.api.reservation.dto.CancelReservationRequest;
import com.wiseaiassignment.api.reservation.dto.CreateReservationRequest;
import com.wiseaiassignment.api.reservation.dto.ReservationResponse;
import com.wiseaiassignment.api.ApiCustomResponse;
import com.wiseaiassignment.application.reservation.ReservationAppService;
import com.wiseaiassignment.application.reservation.dto.ReservationResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController implements ReservationApiSpec {

	private final ReservationAppService reservationAppService;

	@Override
	@GetMapping(path = "")
	public ApiCustomResponse<List<MeetingRoomScheduleResponse>> getReservationsByDate(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			LocalDate date
	) {
		throw new Error("구현 예정");
	}

	@Override
	@GetMapping(path = "/{id}")
	public ApiCustomResponse<ReservationResponse> getReservationById(
			@PathVariable String id
	) {
		throw new Error("구현 예정");
	}

	@Override
	@PostMapping(path = "")
	public ApiCustomResponse<ReservationResponse> reserve(
			@Valid @RequestBody CreateReservationRequest request
	) {
		ReservationResult result = reservationAppService.reserve(request.toCreateCommand());
		return ApiCustomResponse.of(ReservationResponse.from(result));
	}

	@Override
	@DeleteMapping(path = "/{id}")
	public ApiCustomResponse<ReservationResponse> cancel(
			@PathVariable long id,
			@Valid @RequestBody CancelReservationRequest request
	) {
		throw new Error("구현 예정");
	}
}
