package com.kata.warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WarehouseContext {
    private final StockManager stockManager;
    private final OrderManager orderManager;
    private final ReservationManager reservationManager;
    private final List<String> eventLog = new ArrayList<>();
    private double cashBalance;

    public WarehouseContext(TimeProvider timeProvider) {
        this.stockManager = new StockManager();
        this.orderManager = new OrderManager();
        this.reservationManager = new ReservationManager(timeProvider);
    }

    public StockManager getStockManager() {
        return stockManager;
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public ReservationManager getReservationManager() {
        return reservationManager;
    }

    public Map<String, Integer> getStockBySku() {
        return stockManager.getStockBySku();
    }

    public Map<String, Integer> getReservedBySku() {
        return stockManager.getReservedBySku();
    }

    public Map<String, Double> getPriceBySku() {
        return stockManager.getPriceBySku();
    }

    public Map<String, String> getOrderStatus() {
        return orderManager.getOrderStatus();
    }

    public Map<String, String> getOrderSku() {
        return orderManager.getOrderSku();
    }

    public Map<String, Integer> getOrderQty() {
        return orderManager.getOrderQty();
    }

    public Map<String, Reservation> getReservations() {
        return reservationManager.getReservations();
    }

    public List<String> getEventLog() {
        return eventLog;
    }

    public double getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(double cashBalance) {
        this.cashBalance = cashBalance;
    }

    public void expireReservations() {
        List<Reservation> expired = reservationManager.getExpiredReservations();
        for (Reservation reservation : expired) {
            reservation.deactivate();
            stockManager.removeReservation(reservation.getSku(), reservation.getQuantity());
            eventLog.add("reservation " + reservation.getReservationId() + " expired, released " + 
                reservation.getQuantity() + " of " + reservation.getSku());
        }
    }
}
