package com.kata.warehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationManager {
    private static final long MINUTES_TO_MILLIS = 60L * 1000L;
    private final Map<String, Reservation> reservations = new HashMap<>();
    private final TimeProvider timeProvider;
    private int nextReservationNumber;

    public ReservationManager(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
        this.nextReservationNumber = 1;
    }

    public String createReservation(String customer, String sku, int quantity, int minutes) {
        String reservationId = "R" + nextReservationNumber;
        nextReservationNumber++;
        long currentTime = timeProvider.getCurrentTimeMillis();
        long expiryTime = currentTime + (minutes * MINUTES_TO_MILLIS);
        Reservation reservation = new Reservation(reservationId, customer, sku, quantity, expiryTime);
        reservations.put(reservationId, reservation);
        return reservationId;
    }

    public Reservation getReservation(String reservationId) {
        return reservations.get(reservationId);
    }

    public List<Reservation> getExpiredReservations() {
        long currentTime = timeProvider.getCurrentTimeMillis();
        List<Reservation> expired = new ArrayList<>();
        for (Reservation reservation : reservations.values()) {
            if (reservation.isActive() && reservation.isExpired(currentTime)) {
                expired.add(reservation);
            }
        }
        return expired;
    }

    public Map<String, Reservation> getReservations() {
        return reservations;
    }
}
