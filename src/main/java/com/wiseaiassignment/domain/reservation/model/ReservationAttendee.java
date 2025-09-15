package com.wiseaiassignment.domain.reservation.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
		uniqueConstraints = {
				@UniqueConstraint(name = "uq_reservation_user", columnNames = {"reservation_id", "user_id"})
		},
		indexes = {
				@Index(name = "idx_attendee_user", columnList = "user_id")
		}
)
public class ReservationAttendee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "reservation_id", nullable = false)
	private long reservationId;

	@Column(nullable = false)
	private long userId;

	@Getter(AccessLevel.NONE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "reservation_id",
			nullable = false,
			foreignKey = @ForeignKey(name = "fk_reservation_attendee_reservation"),
			insertable = false, updatable = false
	)
	private Reservation reservation;


	public static ReservationAttendee of(Long reservationId, long userId) {
		ReservationAttendee attendee = new ReservationAttendee();
		attendee.reservationId = reservationId;
		attendee.userId = userId;
		return attendee;
	}

	public static List<ReservationAttendee> bulkCreate(Reservation reservation, List<Long> userIds) {
		List<ReservationAttendee> list = userIds.stream()
				.map(userId -> ReservationAttendee.of(reservation.getId(), userId))
				.collect(Collectors.toList());
		list.add(ReservationAttendee.of(reservation.getId(), reservation.getOrganizerId()));
		return list;
	}

}
