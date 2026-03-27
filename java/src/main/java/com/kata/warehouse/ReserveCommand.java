package com.kata.warehouse;

public class ReserveCommand implements Command {
    @Override
    public void execute(WarehouseContext context, String[] parts) {
        String customer = parts[1];
        String sku = parts[2];
        int qty = context.parseInt(parts[3]);
        int expiryMinutes = context.parseInt(parts[4]);
        String reservationId = "R" + context.getNextReservationNumber();
        context.setNextReservationNumber(context.getNextReservationNumber() + 1);

        int onHand = context.getStockBySku().getOrDefault(sku, 0);
        int reserved = context.getReservedBySku().getOrDefault(sku, 0);
        int available = onHand - reserved;

        if (available < qty) {
            context.addEvent("reservation " + reservationId + " failed for " + customer + " - insufficient stock");
            return;
        }

        Reservation reservation = new Reservation(reservationId, customer, sku, qty, expiryMinutes);
        context.getReservations().put(reservationId, reservation);
        context.getReservedBySku().put(sku, reserved + qty);
        context.addEvent("reservation " + reservationId + " created for " + customer + " sku=" + sku + " qty=" + qty + " expires in " + expiryMinutes + " minutes");
    }
}
