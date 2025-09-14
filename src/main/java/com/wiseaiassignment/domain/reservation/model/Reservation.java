package com.wiseaiassignment.domain.reservation.model;

import com.wiseaiassignment.domain.common.exception.DomainException;
import com.wiseaiassignment.domain.common.exception.ExceptionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_reservation_meeting_room"))
	private long meetingRoomId;

	@Column(nullable = false)
	private String meetingRoomName;

	@Column(nullable = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_reservation_user"))
	private long userId;

	@Column(nullable = false)
	private String userEmail;

	@Getter(AccessLevel.NONE)
	@Embedded
	private TimeRange timeRange;

	@Column(nullable = false)
	private List<String> attendeeEmails = List.of();

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReservationStatus status = ReservationStatus.RESERVED;

	@CreatedDate
	private Instant createdAt;

	@LastModifiedDate
	private Instant updatedAt;

	Reservation(
			Long id,
			String title,
			long meetingRoomId,
			String meetingRoomName,
			long userId,
			String userEmail,
			TimeRange timeRange,
			List<String> attendeeEmails
	) {
		this.id = id;
		this.title = title;
		this.meetingRoomId = meetingRoomId;
		this.meetingRoomName = meetingRoomName;
		this.userId = userId;
		this.userEmail = userEmail;
		this.timeRange = timeRange;
		this.attendeeEmails = attendeeEmails;
	}

	public static Reservation create(
			String title,
			long userId,
			String userEmail,
			long meetingRoomId,
			String meetingRoomName,
			LocalDateTime startTime,
			LocalDateTime endTime,
			List<String> attendeeEmails
	) {
		return new Reservation(
				null,
				title,
				meetingRoomId,
				meetingRoomName,
				userId,
				userEmail,
				TimeRange.of(startTime, endTime),
				attendeeEmails
		);
	}

	public LocalDateTime getStartTime() {
		return timeRange.getStartTime();
	}

	public LocalDateTime getEndTime() {
		return timeRange.getEndTime();
	}

	public void cancel(long userId) {
		if (this.userId != userId) {
			throw new DomainException(ExceptionType.NOT_RESERVATION_HOST);
		}
		if (status == ReservationStatus.CANCELED) {
			throw new DomainException(ExceptionType.ALREADY_CANCELED_RESERVATION);
		}
		status = ReservationStatus.CANCELED;
	}

}
