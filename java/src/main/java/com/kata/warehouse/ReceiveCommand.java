package com.kata.warehouse;

public class ReceiveCommand implements Command {
    private final String sku;
    private final int quantity;
    private final double unitCost;

    public ReceiveCommand(String sku, int quantity, double unitCost) {
        this.sku = sku;
        this.quantity = quantity;
        this.unitCost = unitCost;
    }

    @Override
    public void execute(WarehouseContext context) {
        context.getStockManager().addStock(sku, quantity);
        context.setCashBalance(context.getCashBalance() - (quantity * unitCost));
        context.getEventLog().add("received " + quantity + " of " + sku + " at " + unitCost);
    }
}
