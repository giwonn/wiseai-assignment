package com.wiseaiassignment.api.reservation;

import com.wiseaiassignment.api.ApiCustomResponse;
import com.wiseaiassignment.api.reservation.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Reservations", description = "예약 API")
public interface ReservationApiSpec {

	@Operation(summary = "회의실 예약 리스트 조회", description = "특정 일자의 회의실 예약 내역들을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "회의실 예약 리스트 조회 성공")
	ApiCustomResponse<List<ReservationSummaryResponse>> getReservationsByDate(
			@Parameter(description = "조회할 날짜 (yyyy-MM-dd)")
			LocalDate date
	);

	@Operation(summary = "회의실 예약 단건 조회", description = "예약 내역을 단건 조회합니다.")
	@ApiResponse(responseCode = "200", description = "회의실 예약 내역 조회 성공")
	ApiCustomResponse<ReservationResponse> getReservationById(
			long id
	);

	@Operation(summary = "회의실 예약", description = "회의실을 예약을 요청합니다.")
	@ApiResponse(responseCode = "200", description = "회의실 예약 성공")
	ApiCustomResponse<ReservationResponse> reserve(
			CreateReservationRequest request
	);

	@Operation(summary = "회의실 예약 취소", description = "회의실을 예약을 취소합니다.")
	@ApiResponse(responseCode = "200", description = "회의실 예약 취소 성공")
	ApiCustomResponse<Void> cancel(
			long id,
			CancelReservationRequest request
	);

	@Operation(summary = "회의실 예약 변경", description = "회의실을 예약을 변경합니다.")
	@ApiResponse(responseCode = "200", description = "회의실 예약 변경 성공")
	ApiCustomResponse<ReservationResponse> modify(
			long id,
			ChangeReservationRequest request
	);

}
