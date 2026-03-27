package com.kata.warehouse.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReceiveCommandHandler implements CommandHandler {
    private final Map<String, Integer> stockBySku;
    private final List<String> eventLog;
    private double cashBalance;

    public ReceiveCommandHandler(Map<String, Integer> stockBySku, List<String> eventLog, double cashBalance) {
        this.stockBySku = stockBySku;
        this.eventLog = eventLog;
        this.cashBalance = cashBalance;
    }

    @Override
    public void handle(String[] parts) {
        String sku = parts[1];
        int qty = Integer.parseInt(parts[2].trim());
        double unitCost = Double.parseDouble(parts[3].trim());
        int current = stockBySku.getOrDefault(sku, 0);
        stockBySku.put(sku, current + qty);
        cashBalance = cashBalance - (qty * unitCost);
        eventLog.add("received " + qty + " of " + sku + " at " + unitCost);
    }

    @Override
    public List<String> getEventLog() {
        return new ArrayList<>(eventLog);
    }

    public double getCashBalance() {
        return cashBalance;
    }
}
