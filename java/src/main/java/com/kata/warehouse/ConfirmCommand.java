package com.kata.warehouse;

public class ConfirmCommand implements Command {
    @Override
    public void execute(WarehouseContext context, String[] parts) {
        String reservationId = parts[1];
        Reservation reservation = context.getReservations().get(reservationId);

        if (reservation == null) {
            context.addEvent("cannot confirm " + reservationId + " because it does not exist");
            return;
        }

        if (!reservation.isActive()) {
            context.addEvent("cannot confirm " + reservationId + " because it is no longer active");
            return;
        }

        if (reservation.isExpired()) {
            context.addEvent("cannot confirm " + reservationId + " because it has expired");
            return;
        }

        String sku = reservation.getSku();
        int qty = reservation.getQuantity();
        String orderId = "O" + context.getNextOrderNumber();
        context.setNextOrderNumber(context.getNextOrderNumber() + 1);

        int onHand = context.getStockBySku().getOrDefault(sku, 0);
        int reserved = context.getReservedBySku().getOrDefault(sku, 0);
        context.getStockBySku().put(sku, onHand - qty);
        context.getReservedBySku().put(sku, reserved - qty);

        double unitPrice = context.getPriceBySku().getOrDefault(sku, 0.0);
        double orderTotal = unitPrice * qty;
        context.setCashBalance(context.getCashBalance() + orderTotal);

        context.getOrderStatus().put(orderId, "SHIPPED");
        context.getOrderSku().put(orderId, sku);
        context.getOrderQty().put(orderId, qty);

        reservation.deactivate();
        context.addEvent("reservation " + reservationId + " confirmed as order " + orderId + " amount=" + orderTotal);
    }
}
