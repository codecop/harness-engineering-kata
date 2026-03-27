package com.kata.warehouse;

public class ReceiveCommand implements Command {
    @Override
    public void execute(WarehouseContext context, String[] parts) {
        String sku = parts[1];
        int qty = context.parseInt(parts[2]);
        double unitCost = context.parseDouble(parts[3]);
        int current = context.getStockBySku().getOrDefault(sku, 0);
        context.getStockBySku().put(sku, current + qty);
        context.setCashBalance(context.getCashBalance() - (qty * unitCost));
        context.addEvent("received " + qty + " of " + sku + " at " + unitCost);
    }
}
