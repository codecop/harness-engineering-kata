package com.kata.warehouse.reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ReservationService {
    private final ReservationRepository repository;
    private int nextReservationNumber = 2001;

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    public Reservation createReservation(String customer, String sku, int quantity, int expiryMinutes, LocalDateTime currentTime) {
        ReservationId id = new ReservationId("R" + nextReservationNumber);
        nextReservationNumber++;
        LocalDateTime expiryTime = currentTime.plusMinutes(expiryMinutes);
        Reservation reservation = new Reservation(id, customer, sku, quantity, expiryTime);
        repository.save(reservation);
        return reservation;
    }

    public Optional<Reservation> findReservation(ReservationId id) {
        return repository.findById(id);
    }

    public void confirmReservation(ReservationId id) {
        repository.findById(id).ifPresent(Reservation::confirm);
    }

    public void releaseReservation(ReservationId id) {
        repository.findById(id).ifPresent(Reservation::release);
    }

    public List<Reservation> processExpiredReservations(LocalDateTime currentTime) {
        List<Reservation> expired = repository.findExpiredReservations(currentTime);
        expired.forEach(Reservation::expire);
        return expired;
    }

    public int getTotalReservedQuantity(String sku) {
        return repository.getTotalReservedQuantity(sku);
    }
}
