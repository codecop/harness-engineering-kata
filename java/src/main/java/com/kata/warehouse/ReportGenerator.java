package com.kata.warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportGenerator {
    public void printEndOfDayReport(WarehouseContext context) {
        int shipped = 0;
        int backorder = 0;
        int cancelled = 0;
        for (String status : context.getOrderStatus().values()) {
            if ("SHIPPED".equals(status)) {
                shipped++;
            } else if ("BACKORDER".equals(status)) {
                backorder++;
            } else if (status.startsWith("CANCELLED")) {
                cancelled++;
            }
        }

        List<String> lowStock = new ArrayList<>();
        for (Map.Entry<String, Integer> item : context.getStockBySku().entrySet()) {
            if (item.getValue() < 5) {
                lowStock.add(item.getKey());
            }
        }

        System.out.println();
        System.out.println("==== end of day ====");
        System.out.println("orders shipped: " + shipped);
        System.out.println("orders backordered: " + backorder);
        System.out.println("orders cancelled: " + cancelled);
        System.out.println("cash balance: " + String.format("%.2f", context.getCashBalance()));
        System.out.println("low stock skus: " + lowStock);
        System.out.println();
        System.out.println("events:");
        for (String event : context.getEventLog()) {
            System.out.println(" - " + event);
        }
    }
}
