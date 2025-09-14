package com.wiseaiassignment.domain.reservation.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReservationSummary {

	private Long id;

	@Column(nullable = false)
	private String title;

	private long meetingRoomId;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	private ReservationStatus status;

	public static ReservationSummary from(Reservation reservation) {
		ReservationSummary summary = new ReservationSummary();
		summary.id = reservation.getId();
		summary.title = reservation.getTitle();
		summary.meetingRoomId = reservation.getMeetingRoomId();
		summary.startTime = reservation.getStartTime();
		summary.endTime = reservation.getEndTime();
		summary.status = reservation.getStatus();
		return summary;
	}
}
