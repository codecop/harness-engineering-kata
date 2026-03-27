package com.kata.warehouse;

import java.util.HashMap;
import java.util.Map;

public class OrderManager {
    private final Map<String, String> orderStatus = new HashMap<>();
    private final Map<String, String> orderSku = new HashMap<>();
    private final Map<String, Integer> orderQty = new HashMap<>();
    private int nextOrderNumber;

    public OrderManager() {
        this.nextOrderNumber = 1001;
    }

    public String createOrder(String sku, int quantity, String status) {
        String orderId = "O" + nextOrderNumber;
        nextOrderNumber++;
        orderSku.put(orderId, sku);
        orderQty.put(orderId, quantity);
        orderStatus.put(orderId, status);
        return orderId;
    }

    public String getStatus(String orderId) {
        return orderStatus.get(orderId);
    }

    public void setStatus(String orderId, String status) {
        orderStatus.put(orderId, status);
    }

    public String getSku(String orderId) {
        return orderSku.get(orderId);
    }

    public int getQuantity(String orderId) {
        return orderQty.getOrDefault(orderId, 0);
    }

    public Map<String, String> getOrderStatus() {
        return orderStatus;
    }

    public Map<String, String> getOrderSku() {
        return orderSku;
    }

    public Map<String, Integer> getOrderQty() {
        return orderQty;
    }
}
