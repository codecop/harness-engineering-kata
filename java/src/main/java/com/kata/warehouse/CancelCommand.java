package com.kata.warehouse;

public class CancelCommand implements Command {
    @Override
    public void execute(WarehouseContext context, String[] parts) {
        String orderId = parts[1];
        String status = context.getOrderStatus().get(orderId);
        if (status == null) {
            context.addEvent("cannot cancel " + orderId + " because it does not exist");
            return;
        }

        if ("BACKORDER".equals(status)) {
            context.getOrderStatus().put(orderId, "CANCELLED");
            context.addEvent("cancelled backorder " + orderId);
            return;
        }

        if ("SHIPPED".equals(status)) {
            String sku = context.getOrderSku().get(orderId);
            int qty = context.getOrderQty().getOrDefault(orderId, 0);
            int current = context.getStockBySku().getOrDefault(sku, 0);
            context.getStockBySku().put(sku, current + qty);
            double unitPrice = context.getPriceBySku().getOrDefault(sku, 0.0);
            context.setCashBalance(context.getCashBalance() - (unitPrice * qty));
            context.getOrderStatus().put(orderId, "CANCELLED_AFTER_SHIP");
            context.addEvent("cancelled shipped order " + orderId + " with restock");
            return;
        }

        context.addEvent("order " + orderId + " could not be cancelled from state " + status);
    }
}
