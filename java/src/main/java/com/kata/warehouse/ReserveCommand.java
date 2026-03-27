package com.kata.warehouse;

public class ReserveCommand implements Command {
    private final String customer;
    private final String sku;
    private final int quantity;
    private final int minutes;

    public ReserveCommand(String customer, String sku, int quantity, int minutes) {
        this.customer = customer;
        this.sku = sku;
        this.quantity = quantity;
        this.minutes = minutes;
    }

    @Override
    public void execute(WarehouseContext context) {
        context.expireReservations();
        
        StockManager stockManager = context.getStockManager();
        int available = stockManager.getAvailable(sku);
        
        if (available < quantity) {
            context.getEventLog().add("reservation failed for " + customer + " sku=" + sku + " qty=" + quantity + " (insufficient stock)");
            return;
        }
        
        String reservationId = context.getReservationManager().createReservation(customer, sku, quantity, minutes);
        stockManager.addReservation(sku, quantity);
        
        context.getEventLog().add("reservation " + reservationId + " created for " + customer + " sku=" + sku + " qty=" + quantity + " expires_in=" + minutes + "min");
    }
}
