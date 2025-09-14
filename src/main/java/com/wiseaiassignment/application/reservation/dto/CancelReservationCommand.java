package com.wiseaiassignment.application.reservation.dto;

public record CancelReservationCommand(
		long reservationId,
		long userId
) {
}
