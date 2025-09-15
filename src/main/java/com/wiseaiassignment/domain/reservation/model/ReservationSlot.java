package com.wiseaiassignment.domain.reservation.model;

import com.wiseaiassignment.domain.meetingroom.model.MeetingRoom;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(
		indexes = {
				@Index(name = "idx_slots_reservation_id", columnList = "reservation_id")
		},
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"meeting_room_id", "slot_time"})
		}
)
@Entity
public class ReservationSlot {

	public static final int SLOT_INTERVAL_MINUTES = 30;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "meeting_room_id", nullable = false)
	private long meetingRoomId;

	@Column(name = "reservation_id", nullable = false)
	private long reservationId;

	@Column(nullable = false, columnDefinition = "DATETIME(0)")
	private LocalDateTime slotTime;

	@Column(nullable = false)
	private int durationMinutes = SLOT_INTERVAL_MINUTES;

	public static ReservationSlot create(Reservation reservation, LocalDateTime slotTime) {
		ReservationSlot entity = new ReservationSlot();
		entity.meetingRoomId = reservation.getMeetingRoomId();
		entity.reservationId = reservation.getId();
		entity.slotTime = slotTime;
		return entity;
	}

	public static List<ReservationSlot> createBulk(Reservation reservation) {
		List<ReservationSlot> slots = new ArrayList<>();

		LocalDateTime slotTime = reservation.getStartTime();
		while (slotTime.isBefore(reservation.getEndTime())) {
			slots.add(create(reservation, slotTime));
			slotTime = slotTime.plusMinutes(SLOT_INTERVAL_MINUTES);
		}

		return slots;
	}

}
