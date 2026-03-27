package com.kata.warehouse.order;

import com.kata.warehouse.Command;
import com.kata.warehouse.WarehouseContext;
import com.kata.warehouse.shared.Money;

public class CancelCommand implements Command {
    @Override
    public void execute(WarehouseContext context, String[] parts) {
        OrderId orderId = new OrderId(parts[1]);
        Order order = context.getOrderService().getOrder(orderId);
        
        if (order == null) {
            context.getEventLogService().addEvent("cannot cancel " + orderId + " because it does not exist");
            return;
        }

        if (order.isBackorder()) {
            order.setStatus(Order.OrderStatus.CANCELLED);
            context.getEventLogService().addEvent("cancelled backorder " + orderId);
            return;
        }

        if (order.isShipped()) {
            context.getInventoryService().addStock(order.getSku(), order.getQuantity());
            Money refund = context.getOrderService().calculateOrderTotal(order.getSku(), order.getQuantity());
            context.getCashService().deductCash(refund);
            order.setStatus(Order.OrderStatus.CANCELLED_AFTER_SHIP);
            context.getEventLogService().addEvent("cancelled shipped order " + orderId + " with restock");
            return;
        }

        context.getEventLogService().addEvent("order " + orderId + " could not be cancelled from state " + order.getStatus());
    }
}
