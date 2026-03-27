package com.kata.warehouse.reservation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ReservationRepository {
    private final Map<ReservationId, Reservation> reservations = new HashMap<>();

    public Optional<Reservation> findById(ReservationId reservationId) {
        return Optional.ofNullable(reservations.get(reservationId));
    }

    public void save(Reservation reservation) {
        reservations.put(reservation.getReservationId(), reservation);
    }

    public Collection<Reservation> findAll() {
        return reservations.values();
    }
}
