package com.kata.warehouse;

public class ConfirmCommand implements Command {
    private final String reservationId;

    public ConfirmCommand(String reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public void execute(WarehouseContext context) {
        context.expireReservations();
        
        Reservation reservation = context.getReservationManager().getReservation(reservationId);
        if (reservation == null) {
            context.getEventLog().add("cannot confirm " + reservationId + " because it does not exist");
            return;
        }
        
        if (!reservation.isActive()) {
            context.getEventLog().add("cannot confirm " + reservationId + " because it is no longer active");
            return;
        }
        
        String sku = reservation.getSku();
        int quantity = reservation.getQuantity();
        
        StockManager stockManager = context.getStockManager();
        stockManager.removeStock(sku, quantity);
        stockManager.removeReservation(sku, quantity);
        
        double unitPrice = stockManager.getPrice(sku);
        double orderTotal = unitPrice * quantity;
        context.setCashBalance(context.getCashBalance() + orderTotal);
        
        String orderId = context.getOrderManager().createOrder(sku, quantity, "SHIPPED");
        reservation.deactivate();
        
        context.getEventLog().add("reservation " + reservationId + " confirmed as order " + orderId + " for " + reservation.getCustomer() + " amount=" + orderTotal);
    }
}
