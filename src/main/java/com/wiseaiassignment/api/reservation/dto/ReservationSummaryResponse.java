package com.wiseaiassignment.api.reservation.dto;

import com.wiseaiassignment.application.reservation.dto.ReservationSummaryResult;
import com.wiseaiassignment.domain.reservation.model.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "예약 요약 정보 응답")
public record ReservationSummaryResponse(
		@Schema(description = "예약 ID", example = "1")
		long reservationId,

		@Schema(description = "예약 제목", example = "주간회의")
		String reservationTitle,

		@Schema(description = "회의실 ID", example = "1")
		long roomId,

		@Schema(description = "예약 시작 시간", example = "2025-09-15T09:00:00")
		LocalDateTime startTime,

		@Schema(description = "예약 종료 시간", example = "2025-09-15T10:00:00")
		LocalDateTime endTime,

		@Schema(description = "예약 상태", example = "RESERVED")
		ReservationStatus status
) {
	public static ReservationSummaryResponse from(ReservationSummaryResult result) {
		return new ReservationSummaryResponse(
				result.reservationId(),
				result.reservationTitle(),
				result.roomId(),
				result.startTime(),
				result.endTime(),
				result.status()
		);
	}
}
