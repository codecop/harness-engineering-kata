package com.kata.warehouse.reservation;

import java.time.LocalDateTime;

public class Reservation {
    private final ReservationId id;
    private final String customer;
    private final String sku;
    private final int quantity;
    private final LocalDateTime expiryTime;
    private ReservationStatus status;

    public Reservation(ReservationId id, String customer, String sku, int quantity, LocalDateTime expiryTime) {
        this.id = id;
        this.customer = customer;
        this.sku = sku;
        this.quantity = quantity;
        this.expiryTime = expiryTime;
        this.status = ReservationStatus.ACTIVE;
    }

    public ReservationId getId() {
        return id;
    }

    public String getCustomer() {
        return customer;
    }

    public String getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public boolean isExpired(LocalDateTime currentTime) {
        return currentTime.isAfter(expiryTime) && status == ReservationStatus.ACTIVE;
    }

    public void expire() {
        this.status = ReservationStatus.EXPIRED;
    }

    public void confirm() {
        this.status = ReservationStatus.CONFIRMED;
    }

    public void release() {
        this.status = ReservationStatus.RELEASED;
    }

    public boolean isActive() {
        return status == ReservationStatus.ACTIVE;
    }
}
