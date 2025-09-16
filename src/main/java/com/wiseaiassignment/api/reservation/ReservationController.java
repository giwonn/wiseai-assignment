package com.wiseaiassignment.api.reservation;

import com.wiseaiassignment.api.reservation.dto.*;
import com.wiseaiassignment.api.ApiCustomResponse;
import com.wiseaiassignment.application.reservation.ReservationAppService;
import com.wiseaiassignment.application.reservation.dto.ChangeReservationCommand;
import com.wiseaiassignment.application.reservation.dto.ReservationResult;
import com.wiseaiassignment.application.reservation.dto.ReservationSummaryResult;
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
	@GetMapping(path = "/summary")
	public ApiCustomResponse<List<ReservationSummaryResponse>> getReservationsByDate(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			LocalDate date
	) {
		List<ReservationSummaryResult> results = reservationAppService.findByDate(date);
		return ApiCustomResponse.of(results.stream().map(ReservationSummaryResponse::from).toList());
	}

	@Override
	@GetMapping(path = "/{id}")
	public ApiCustomResponse<ReservationResponse> getReservationById(
			@PathVariable long id
	) {
		ReservationResult result = reservationAppService.findById(id);
		return ApiCustomResponse.of(ReservationResponse.from(result));
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
	@PostMapping(path = "/{id}/cancel")
	public ApiCustomResponse<Void> cancel(
			@PathVariable long id,
			@Valid @RequestBody CancelReservationRequest request
	) {
		reservationAppService.cancel(request.toCancelCommand(id));
		return ApiCustomResponse.empty();
	}

	@Override
	@PatchMapping(path = "/{id}")
	public ApiCustomResponse<ReservationResponse> modify(
			@PathVariable long id,
			@Valid @RequestBody ChangeReservationRequest request
	) {
		ReservationResult result = reservationAppService.changeReservation(request.toChangeCommand(id));
		return ApiCustomResponse.of(ReservationResponse.from(result));
	}
}
