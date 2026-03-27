package com.kata.warehouse.domain.entity;

import com.kata.warehouse.domain.valueobject.OrderId;
import com.kata.warehouse.domain.valueobject.Quantity;
import com.kata.warehouse.domain.valueobject.SKU;

public class Order {
    private final OrderId orderId;
    private final SKU sku;
    private final Quantity quantity;
    private OrderStatus status;

    public Order(OrderId orderId, SKU sku, Quantity quantity, OrderStatus status) {
        this.orderId = orderId;
        this.sku = sku;
        this.quantity = quantity;
        this.status = status;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public SKU getSku() {
        return sku;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public boolean isBackorder() {
        return status == OrderStatus.BACKORDER;
    }

    public boolean isShipped() {
        return status == OrderStatus.SHIPPED;
    }

    public boolean isCancelled() {
        return status == OrderStatus.CANCELLED || status == OrderStatus.CANCELLED_AFTER_SHIP;
    }

    public enum OrderStatus {
        BACKORDER,
        SHIPPED,
        CANCELLED,
        CANCELLED_AFTER_SHIP
    }
}
