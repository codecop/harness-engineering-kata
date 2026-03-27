package com.kata.warehouse;

public class CancelCommand implements Command {
    private final String orderId;

    public CancelCommand(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public void execute(WarehouseContext context) {
        OrderManager orderManager = context.getOrderManager();
        String status = orderManager.getStatus(orderId);
        
        if (status == null) {
            context.getEventLog().add("cannot cancel " + orderId + " because it does not exist");
            return;
        }

        if ("BACKORDER".equals(status)) {
            orderManager.setStatus(orderId, "CANCELLED");
            context.getEventLog().add("cancelled backorder " + orderId);
            return;
        }

        if ("SHIPPED".equals(status)) {
            String sku = orderManager.getSku(orderId);
            int qty = orderManager.getQuantity(orderId);
            context.getStockManager().addStock(sku, qty);
            double unitPrice = context.getStockManager().getPrice(sku);
            context.setCashBalance(context.getCashBalance() - (unitPrice * qty));
            orderManager.setStatus(orderId, "CANCELLED_AFTER_SHIP");
            context.getEventLog().add("cancelled shipped order " + orderId + " with restock");
            return;
        }

        context.getEventLog().add("order " + orderId + " could not be cancelled from state " + status);
    }
}
