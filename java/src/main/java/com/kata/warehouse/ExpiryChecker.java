package com.kata.warehouse;

import java.util.ArrayList;
import java.util.List;

public class ExpiryChecker {
    public void checkExpiredReservations(WarehouseContext context) {
        List<Reservation> expiredList = new ArrayList<>();
        for (Reservation reservation : context.getReservations().values()) {
            if (reservation.isActive() && reservation.isExpired()) {
                expiredList.add(reservation);
            }
        }

        for (Reservation reservation : expiredList) {
            String sku = reservation.getSku();
            int qty = reservation.getQuantity();
            int reserved = context.getReservedBySku().getOrDefault(sku, 0);
            context.getReservedBySku().put(sku, reserved - qty);
            reservation.deactivate();
            context.addEvent("reservation " + reservation.getReservationId() + " expired and released");
        }
    }
}
