package com.kata.warehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarehouseContext {
    private final Map<String, Integer> stockBySku = new HashMap<>();
    private final Map<String, Integer> reservedBySku = new HashMap<>();
    private final Map<String, Double> priceBySku = new HashMap<>();
    private final Map<String, String> orderStatus = new HashMap<>();
    private final Map<String, String> orderSku = new HashMap<>();
    private final Map<String, Integer> orderQty = new HashMap<>();
    private final List<String> eventLog = new ArrayList<>();
    private final Map<String, Reservation> reservations = new HashMap<>();
    private double cashBalance;
    private int nextOrderNumber;
    private int nextReservationNumber;

    public Map<String, Integer> getStockBySku() {
        return stockBySku;
    }

    public Map<String, Integer> getReservedBySku() {
        return reservedBySku;
    }

    public Map<String, Double> getPriceBySku() {
        return priceBySku;
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

    public List<String> getEventLog() {
        return eventLog;
    }

    public Map<String, Reservation> getReservations() {
        return reservations;
    }

    public double getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(double cashBalance) {
        this.cashBalance = cashBalance;
    }

    public int getNextOrderNumber() {
        return nextOrderNumber;
    }

    public void setNextOrderNumber(int nextOrderNumber) {
        this.nextOrderNumber = nextOrderNumber;
    }

    public int getNextReservationNumber() {
        return nextReservationNumber;
    }

    public void setNextReservationNumber(int nextReservationNumber) {
        this.nextReservationNumber = nextReservationNumber;
    }

    public void addEvent(String event) {
        eventLog.add(event);
    }

    public int parseInt(String value) {
        return Integer.parseInt(value.trim());
    }

    public double parseDouble(String value) {
        return Double.parseDouble(value.trim());
    }
}
