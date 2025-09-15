package com.wiseaiassignment.domain.reservation.repository;

import com.wiseaiassignment.domain.reservation.model.ReservationAttendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationAttendeeRepository extends JpaRepository<ReservationAttendee, Long> {
}
