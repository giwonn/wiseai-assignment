package com.wiseaiassignment.domain.reservation.model;

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

	@Column(nullable = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_reservation_slot_meeting_room"))
	private long meetingRoomId;

	@Column(nullable = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_reservation_slot_reservation"))
	private long reservationId;

	@Column(nullable = false, columnDefinition = "DATETIME(0)")
	private LocalDateTime slotTime;

	@Column(nullable = false)
	private int durationMinutes = SLOT_INTERVAL_MINUTES;

	public static ReservationSlot create(long meetingRoomId, long reservationId, LocalDateTime slotTime) {
		ReservationSlot entity = new ReservationSlot();
		entity.meetingRoomId = meetingRoomId;
		entity.reservationId = reservationId;
		entity.slotTime = slotTime;
		return entity;
	}

	public static List<ReservationSlot> createBulk(Reservation reservation) {
		List<ReservationSlot> slots = new ArrayList<>();

		LocalDateTime slotTime = reservation.getStartTime();
		while (slotTime.isBefore(reservation.getEndTime())) {
			slots.add(create(reservation.getMeetingRoomId(), reservation.getId(), slotTime));
			slotTime = slotTime.plusMinutes(SLOT_INTERVAL_MINUTES);
		}

		return slots;
	}

}
