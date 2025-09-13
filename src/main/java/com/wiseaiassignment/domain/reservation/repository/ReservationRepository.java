package com.wiseaiassignment.domain.reservation.repository;

import com.wiseaiassignment.domain.reservation.model.Reservation;
import com.wiseaiassignment.domain.reservation.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	@Query("SELECT r FROM Reservation r	WHERE r.meetingRoomId = :roomId AND r.timeRange.startTime < :endTime AND r.timeRange.endTime > :startTime")
	@Query("SELECT r FROM Reservation r WHERE r.meetingRoomId = :roomId " +
			"AND r.timeRange.startTime < :endTime " +
			"AND r.timeRange.endTime > :startTime")
	List<Reservation> findConflictReservations(
			@Param("roomId") Long meetingRoomId,
			@Param("startTime") LocalDateTime startTime,
			@Param("endTime") LocalDateTime endTime
	);
}
