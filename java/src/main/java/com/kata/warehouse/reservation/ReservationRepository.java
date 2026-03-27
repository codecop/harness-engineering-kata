package com.kata.warehouse.reservation;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReservationRepository {
    private final Map<ReservationId, Reservation> reservations = new HashMap<>();

    public void save(Reservation reservation) {
        reservations.put(reservation.getId(), reservation);
    }

    public Optional<Reservation> findById(ReservationId id) {
        return Optional.ofNullable(reservations.get(id));
    }

    public List<Reservation> findExpiredReservations(LocalDateTime currentTime) {
        return reservations.values().stream()
                .filter(r -> r.isExpired(currentTime))
                .collect(Collectors.toList());
    }

    public int getTotalReservedQuantity(String sku) {
        return reservations.values().stream()
                .filter(r -> r.getSku().equals(sku) && r.isActive())
                .mapToInt(Reservation::getQuantity)
                .sum();
    }
}
