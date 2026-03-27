package com.kata.warehouse;

public class Reservation {
    private final String reservationId;
    private final String customer;
    private final String sku;
    private final int quantity;
    private final long expiryTimeMillis;
    private boolean active;

    public Reservation(String reservationId, String customer, String sku, int quantity, int expiryMinutes) {
        this.reservationId = reservationId;
        this.customer = customer;
        this.sku = sku;
        this.quantity = quantity;
        this.expiryTimeMillis = System.currentTimeMillis() + (expiryMinutes * 60L * 1000L);
        this.active = true;
    }

    public String getReservationId() {
        return reservationId;
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
