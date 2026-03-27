package com.kata.warehouse.order;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OrderRepository {
    private final Map<OrderId, Order> orders = new HashMap<>();

    public Optional<Order> findById(OrderId orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    public void save(Order order) {
        orders.put(order.getOrderId(), order);
    }
}
