package com.wiseaiassignment.domain.reservation.repository;

import com.wiseaiassignment.domain.reservation.model.ReservationSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationSlotRepository extends JpaRepository<ReservationSlot, Long> {
}
