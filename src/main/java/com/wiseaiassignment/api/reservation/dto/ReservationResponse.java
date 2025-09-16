package com.wiseaiassignment.api.reservation.dto;

import com.wiseaiassignment.application.reservation.dto.ReservationResult;
import com.wiseaiassignment.domain.reservation.model.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "예약 상세 정보 응답")
public record ReservationResponse(
		@Schema(description = "예약 ID", example = "1")
		long reservationId,

		@Schema(description = "예약 제목", example = "주간회의")
		String reservationTitle,

		@Schema(description = "예약 상태", example = "RESERVED")
		ReservationStatus status,

		@Schema(description = "회의실 ID", example = "1")
		long roomId,

		@Schema(description = "회의실 이름", example = "회의실1")
		String roomName,

		@Schema(description = "예약 시작 시간", example = "2025-09-15T09:00:00")
		LocalDateTime startTime,

		@Schema(description = "예약 종료 시간", example = "2025-09-15T10:00:00")
		LocalDateTime endTime,

		@Schema(description = "예약자 ID", example = "1")
		long organizerId,

		@Schema(description = "참여자 ID 목록", example = "[]")
		List<Long> attendeeUserIds
) {

	public static ReservationResponse from(ReservationResult result) {
		return new ReservationResponse(
				result.reservationId(),
				result.reservationTitle(),
				result.status(),
				result.roomId(),
				result.roomName(),
				result.startTime(),
				result.endTime(),
				result.organizerId(),
				result.attendeeUserIds()
		);
	}
}
