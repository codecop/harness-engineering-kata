package com.kata.warehouse;

public class CountCommand implements Command {
    @Override
    public void execute(WarehouseContext context, String[] parts) {
        String sku = parts[1];
        int onHand = context.getStockBySku().getOrDefault(sku, 0);
        int reserved = context.getReservedBySku().getOrDefault(sku, 0);
        int available = onHand - reserved;
        context.addEvent("count " + sku + " onHand=" + onHand + " reserved=" + reserved + " available=" + available);
    }
}
