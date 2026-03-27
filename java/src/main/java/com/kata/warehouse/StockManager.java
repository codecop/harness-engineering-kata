package com.kata.warehouse;

import java.util.HashMap;
import java.util.Map;

public class StockManager {
    private final Map<String, Integer> stockBySku = new HashMap<>();
    private final Map<String, Integer> reservedBySku = new HashMap<>();
    private final Map<String, Double> priceBySku = new HashMap<>();

    public int getOnHand(String sku) {
        return stockBySku.getOrDefault(sku, 0);
    }

    public int getReserved(String sku) {
        return reservedBySku.getOrDefault(sku, 0);
    }

    public int getAvailable(String sku) {
        return getOnHand(sku) - getReserved(sku);
    }

    public void addStock(String sku, int quantity) {
        int current = getOnHand(sku);
        stockBySku.put(sku, current + quantity);
    }

    public void removeStock(String sku, int quantity) {
        int current = getOnHand(sku);
        stockBySku.put(sku, current - quantity);
    }

    public void addReservation(String sku, int quantity) {
        int current = getReserved(sku);
        reservedBySku.put(sku, current + quantity);
    }

    public void removeReservation(String sku, int quantity) {
        int current = getReserved(sku);
        reservedBySku.put(sku, current - quantity);
    }

    public double getPrice(String sku) {
        return priceBySku.getOrDefault(sku, 0.0);
    }

    public void setPrice(String sku, double price) {
        priceBySku.put(sku, price);
    }

    public void setStock(String sku, int quantity) {
        stockBySku.put(sku, quantity);
    }

    public void setReserved(String sku, int quantity) {
        reservedBySku.put(sku, quantity);
    }

    public Map<String, Integer> getStockBySku() {
        return stockBySku;
    }

    public Map<String, Integer> getReservedBySku() {
        return reservedBySku;
    }

    public Map<String, Double> getPriceBySku() {
        return priceBySku;
    }
}
