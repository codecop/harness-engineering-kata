package com.kata.warehouse;

public class SellCommand implements Command {
    @Override
    public void execute(WarehouseContext context, String[] parts) {
        String customer = parts[1];
        String sku = parts[2];
        int qty = context.parseInt(parts[3]);
        String orderId = "O" + context.getNextOrderNumber();
        context.setNextOrderNumber(context.getNextOrderNumber() + 1);
        context.getOrderSku().put(orderId, sku);
        context.getOrderQty().put(orderId, qty);

        int onHand = context.getStockBySku().getOrDefault(sku, 0);
        int reserved = context.getReservedBySku().getOrDefault(sku, 0);
        int available = onHand - reserved;
        if (available < qty) {
            context.getOrderStatus().put(orderId, "BACKORDER");
            context.addEvent("order " + orderId + " backordered for " + customer + " sku=" + sku + " qty=" + qty);
        } else {
            context.getStockBySku().put(sku, onHand - qty);
            double unitPrice = context.getPriceBySku().getOrDefault(sku, 0.0);
            double orderTotal = unitPrice * qty;
            context.setCashBalance(context.getCashBalance() + orderTotal);
            context.getOrderStatus().put(orderId, "SHIPPED");
            context.addEvent("order " + orderId + " shipped to " + customer + " amount=" + orderTotal);
        }
    }
}
