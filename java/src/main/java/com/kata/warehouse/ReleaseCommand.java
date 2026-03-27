package com.kata.warehouse;

public class ReleaseCommand implements Command {
    @Override
    public void execute(WarehouseContext context, String[] parts) {
        String reservationId = parts[1];
        Reservation reservation = context.getReservations().get(reservationId);

        if (reservation == null) {
            context.addEvent("cannot release " + reservationId + " because it does not exist");
            return;
        }

        if (!reservation.isActive()) {
            context.addEvent("cannot release " + reservationId + " because it is no longer active");
            return;
        }

        String sku = reservation.getSku();
        int qty = reservation.getQuantity();
        int reserved = context.getReservedBySku().getOrDefault(sku, 0);
        context.getReservedBySku().put(sku, reserved - qty);

        reservation.deactivate();
        context.addEvent("reservation " + reservationId + " released");
    }
}
