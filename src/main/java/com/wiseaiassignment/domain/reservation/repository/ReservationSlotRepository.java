package com.wiseaiassignment.domain.reservation.repository;

import com.wiseaiassignment.domain.reservation.model.ReservationSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationSlotRepository extends JpaRepository<ReservationSlot, Long> {
	List<ReservationSlot> findByReservationId(long reservationId);

	long deleteByReservationId(long reservationId);
}
