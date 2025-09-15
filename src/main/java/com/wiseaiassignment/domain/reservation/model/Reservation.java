package com.wiseaiassignment.domain.reservation.model;

import com.wiseaiassignment.domain.common.exception.DomainException;
import com.wiseaiassignment.domain.common.exception.ExceptionType;
import com.wiseaiassignment.domain.meetingroom.model.MeetingRoom;
import com.wiseaiassignment.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Getter(AccessLevel.NONE)
	@Embedded
	private TimeRange timeRange;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReservationStatus status = ReservationStatus.RESERVED;

	@Column(name = "meeting_room_id", nullable = false)
	private long meetingRoomId;


	@Column(name = "organizer_id", nullable = false)
	private long organizerId;

	@CreationTimestamp
	private Instant createdAt;

	@UpdateTimestamp
	private Instant updatedAt;

	@OneToMany(mappedBy = "reservation")
	private List<ReservationAttendee> attendees = new ArrayList<>();

	@Getter(AccessLevel.NONE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "meeting_room_id",
			nullable = false,
			foreignKey = @ForeignKey(name = "fk_reservation_meeting_room"),
			insertable=false, updatable=false
	)
	private MeetingRoom meetingRoom;

	@Getter(AccessLevel.NONE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "organizer_id",
			nullable = false,
			foreignKey = @ForeignKey(name = "fk_reservation_user"),
			insertable=false, updatable=false
	)
	private User organizer;

	Reservation(
			Long id,
			String title,
			long meetingRoomId,
			long organizerId,
			TimeRange timeRange
	) {
		this.id = id;
		this.title = title;
		this.meetingRoomId = meetingRoomId;
		this.organizerId = organizerId;
		this.timeRange = timeRange;
	}

	public static Reservation create(
			String title,
			LocalDateTime startTime,
			LocalDateTime endTime,
			long meetingRoomId,
			long organizerId
	) {
		return new Reservation(
				null,
				title,
				meetingRoomId,
				organizerId,
				TimeRange.of(startTime, endTime)
		);
	}

	public LocalDateTime getStartTime() {
		return timeRange.getStartTime();
	}

	public LocalDateTime getEndTime() {
		return timeRange.getEndTime();
	}

	public List<Long> getAttendeeUserIds() {
		return attendees.stream()
				.map(ReservationAttendee::getUserId)
				.toList();
	}

	public void cancel(long userId) {
		if (organizerId != userId) {
			throw new DomainException(ExceptionType.NOT_RESERVATION_HOST);
		}
		if (status == ReservationStatus.CANCELED) {
			throw new DomainException(ExceptionType.ALREADY_CANCELED_RESERVATION);
		}
		status = ReservationStatus.CANCELED;
	}

}
