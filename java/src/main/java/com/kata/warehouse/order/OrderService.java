package com.kata.warehouse.order;

import com.kata.warehouse.inventory.InventoryService;
import com.kata.warehouse.shared.Money;
import com.kata.warehouse.shared.Quantity;
import com.kata.warehouse.shared.SKU;

public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;
    private int nextOrderNumber = 0;

    public OrderService(OrderRepository orderRepository, InventoryService inventoryService) {
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
    }

    public void setNextOrderNumber(int nextOrderNumber) {
        this.nextOrderNumber = nextOrderNumber;
    }

    public OrderId generateOrderId() {
        return new OrderId("O" + nextOrderNumber++);
    }

    public Order createOrder(OrderId orderId, SKU sku, Quantity quantity, Order.OrderStatus status) {
        Order order = new Order(orderId, sku, quantity, status);
        orderRepository.save(order);
        return order;
    }

    public Money calculateOrderTotal(SKU sku, Quantity quantity) {
        Money unitPrice = inventoryService.getUnitPrice(sku);
        return unitPrice.multiply(quantity.getValue());
    }

    public boolean cancelOrder(OrderId orderId) {
        return orderRepository.findById(orderId).map(order -> {
            if (order.isBackorder()) {
                order.setStatus(Order.OrderStatus.CANCELLED);
                orderRepository.save(order);
                return true;
            } else if (order.isShipped()) {
                inventoryService.addStock(order.getSku(), order.getQuantity());
                order.setStatus(Order.OrderStatus.CANCELLED_AFTER_SHIP);
                orderRepository.save(order);
                return true;
            }
            return false;
        }).orElse(false);
    }

    public Order getOrder(OrderId orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }
}
