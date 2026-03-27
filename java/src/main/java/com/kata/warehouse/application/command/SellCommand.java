package com.kata.warehouse.application.command;

import com.kata.warehouse.WarehouseContext;
import com.kata.warehouse.domain.entity.Order;
import com.kata.warehouse.domain.valueobject.Money;
import com.kata.warehouse.domain.valueobject.OrderId;
import com.kata.warehouse.domain.valueobject.Quantity;
import com.kata.warehouse.domain.valueobject.SKU;

public class SellCommand implements Command {
    @Override
    public void execute(WarehouseContext context, String[] parts) {
        String customer = parts[1];
        SKU sku = new SKU(parts[2]);
        Quantity qty = new Quantity(context.parseInt(parts[3]));
        
        OrderId orderId = context.getOrderService().generateOrderId();
        
        if (context.getInventoryService().hasAvailableStock(sku, qty)) {
            context.getInventoryService().removeStock(sku, qty);
            Money orderTotal = context.getOrderService().calculateOrderTotal(sku, qty);
            context.getCashService().addCash(orderTotal);
            context.getOrderService().createOrder(orderId, sku, qty, Order.OrderStatus.SHIPPED);
            context.getEventLogService().addEvent("order " + orderId + " shipped to " + customer + " amount=" + orderTotal);
        } else {
            context.getOrderService().createOrder(orderId, sku, qty, Order.OrderStatus.BACKORDER);
            context.getEventLogService().addEvent("order " + orderId + " backordered for " + customer + " sku=" + sku + " qty=" + qty);
        }
    }
}
