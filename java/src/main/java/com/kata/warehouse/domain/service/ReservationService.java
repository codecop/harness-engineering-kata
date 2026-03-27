package com.kata.warehouse.domain.service;

import com.kata.warehouse.domain.entity.Reservation;
import com.kata.warehouse.domain.repository.ReservationRepository;
import com.kata.warehouse.domain.valueobject.Quantity;
import com.kata.warehouse.domain.valueobject.ReservationId;
import com.kata.warehouse.domain.valueobject.SKU;

public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final InventoryService inventoryService;
    private int nextReservationNumber = 0;

    public ReservationService(ReservationRepository reservationRepository, InventoryService inventoryService) {
        this.reservationRepository = reservationRepository;
        this.inventoryService = inventoryService;
    }

    public void setNextReservationNumber(int nextReservationNumber) {
        this.nextReservationNumber = nextReservationNumber;
    }

    public ReservationId generateReservationId() {
        return new ReservationId("R" + nextReservationNumber++);
    }

    public Reservation createReservation(ReservationId reservationId, String customer, SKU sku, Quantity quantity, int expiryMinutes) {
        if (!inventoryService.hasAvailableStock(sku, quantity)) {
            return null;
        }

        Reservation reservation = new Reservation(reservationId, customer, sku, quantity, expiryMinutes);
        reservationRepository.save(reservation);
        inventoryService.reserveStock(sku, quantity);
        return reservation;
    }

    public boolean releaseReservation(ReservationId reservationId) {
        return reservationRepository.findById(reservationId).map(reservation -> {
            if (!reservation.isActive()) {
                return false;
            }
            inventoryService.releaseReservedStock(reservation.getSku(), reservation.getQuantity());
            reservation.deactivate();
            reservationRepository.save(reservation);
            return true;
        }).orElse(false);
    }

    public Reservation getReservation(ReservationId reservationId) {
        return reservationRepository.findById(reservationId).orElse(null);
    }

    public void checkAndReleaseExpiredReservations() {
        for (Reservation reservation : reservationRepository.findAll()) {
            if (reservation.isActive() && reservation.isExpired()) {
                inventoryService.releaseReservedStock(reservation.getSku(), reservation.getQuantity());
                reservation.deactivate();
                reservationRepository.save(reservation);
            }
        }
    }

    public void checkAndReleaseExpiredReservations(com.kata.warehouse.domain.service.EventLogService eventLogService) {
        for (Reservation reservation : reservationRepository.findAll()) {
            if (reservation.isActive() && reservation.isExpired()) {
                inventoryService.releaseReservedStock(reservation.getSku(), reservation.getQuantity());
                reservation.deactivate();
                reservationRepository.save(reservation);
                eventLogService.addEvent("reservation " + reservation.getReservationId() + " expired and released");
            }
        }
    }
}
