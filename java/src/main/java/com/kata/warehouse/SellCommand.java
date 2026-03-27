package com.kata.warehouse;

public class SellCommand implements Command {
    private final String customer;
    private final String sku;
    private final int quantity;

    public SellCommand(String customer, String sku, int quantity) {
        this.customer = customer;
        this.sku = sku;
        this.quantity = quantity;
    }

    @Override
    public void execute(WarehouseContext context) {
        context.expireReservations();
        
        StockManager stockManager = context.getStockManager();
        int available = stockManager.getAvailable(sku);
        
        if (available < quantity) {
            String orderId = context.getOrderManager().createOrder(sku, quantity, "BACKORDER");
            context.getEventLog().add("order " + orderId + " backordered for " + customer + " sku=" + sku + " qty=" + quantity);
        } else {
            stockManager.removeStock(sku, quantity);
            double unitPrice = stockManager.getPrice(sku);
            double orderTotal = unitPrice * quantity;
            context.setCashBalance(context.getCashBalance() + orderTotal);
            String orderId = context.getOrderManager().createOrder(sku, quantity, "SHIPPED");
            context.getEventLog().add("order " + orderId + " shipped to " + customer + " amount=" + orderTotal);
        }
    }
}
