package com.wiseaiassignment.domain.reservation.repository;

import com.wiseaiassignment.domain.reservation.model.Reservation;
import com.wiseaiassignment.domain.reservation.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	@Query("SELECT DISTINCT r " +
			"FROM Reservation r " +
			"LEFT JOIN FETCH r.attendees " +
			"WHERE r.id = :id")
	Optional<Reservation> findByIdWithAttendees(@Param("id") Long id);

	@Query("SELECT r " +
			"FROM Reservation r " +
			"WHERE r.status = :status " +
			"AND r.timeRange.startTime < :endOfDay " +
			"AND r.timeRange.endTime > :startOfDay")
	List<Reservation> findByStatusAndDate(
			@Param("status") ReservationStatus status,
			@Param("startOfDay") LocalDateTime startOfDay,
			@Param("endOfDay") LocalDateTime endOfDay
	);


	@Query("SELECT r FROM Reservation r WHERE r.meetingRoomId = :roomId " +
			"AND r.timeRange.startTime < :endTime " +
			"AND r.timeRange.endTime > :startTime")
	List<Reservation> findConflictReservations(
			@Param("roomId") Long meetingRoomId,
			@Param("startTime") LocalDateTime startTime,
			@Param("endTime") LocalDateTime endTime
	);
}
