package com.kata.warehouse.reservation;

import com.kata.warehouse.shared.Quantity;
import com.kata.warehouse.shared.SKU;

public class Reservation {
    private final ReservationId reservationId;
    private final String customer;
    private final SKU sku;
    private final Quantity quantity;
    private final long expiryTimeMillis;
    private boolean active;

    public Reservation(ReservationId reservationId, String customer, SKU sku, Quantity quantity, int expiryMinutes) {
        this.reservationId = reservationId;
        this.customer = customer;
        this.sku = sku;
        this.quantity = quantity;
        this.expiryTimeMillis = System.currentTimeMillis() + (expiryMinutes * 60L * 1000L);
        this.active = true;
    }

    public ReservationId getReservationId() {
        return reservationId;
    }

    public String getCustomer() {
        return customer;
    }

    public SKU getSku() {
        return sku;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTimeMillis;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }
}
