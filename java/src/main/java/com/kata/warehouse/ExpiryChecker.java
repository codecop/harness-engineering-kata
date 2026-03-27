package com.kata.warehouse;

public class ExpiryChecker {
    public void checkExpiredReservations(WarehouseContext context) {
        context.getReservationService().checkAndReleaseExpiredReservations(context.getEventLogService());
    }
}
