package com.kata.warehouse;

public class CountCommand implements Command {
    private final String sku;

    public CountCommand(String sku) {
        this.sku = sku;
    }

    @Override
    public void execute(WarehouseContext context) {
        context.expireReservations();
        
        StockManager stockManager = context.getStockManager();
        int onHand = stockManager.getOnHand(sku);
        int reserved = stockManager.getReserved(sku);
        int available = stockManager.getAvailable(sku);
        context.getEventLog().add("count " + sku + " onHand=" + onHand + " reserved=" + reserved + " available=" + available);
    }
}
