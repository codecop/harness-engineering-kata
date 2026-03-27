package com.kata.warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WarehouseDeskApp {
    private final WarehouseContext context;
    private final CommandFactory commandFactory;

    public WarehouseDeskApp() {
        this(new SystemTimeProvider());
    }

    public WarehouseDeskApp(TimeProvider timeProvider) {
        this.context = new WarehouseContext(timeProvider);
        this.commandFactory = new CommandFactory();
    }

    public void seedData() {
        StockManager stockManager = context.getStockManager();
        stockManager.setStock("PEN-BLACK", 40);
        stockManager.setStock("PEN-BLUE", 25);
        stockManager.setStock("NOTE-A5", 15);
        stockManager.setStock("STAPLER", 4);

        stockManager.setReserved("PEN-BLACK", 0);
        stockManager.setReserved("PEN-BLUE", 0);
        stockManager.setReserved("NOTE-A5", 0);
        stockManager.setReserved("STAPLER", 0);

        stockManager.setPrice("PEN-BLACK", 1.5);
        stockManager.setPrice("PEN-BLUE", 1.6);
        stockManager.setPrice("NOTE-A5", 4.0);
        stockManager.setPrice("STAPLER", 12.0);

        context.setCashBalance(300.0);
    }

    public void runDemoDay() {
        List<String> commands = List.of(
            "RECV;NOTE-A5;5;2.20",
            "SELL;alice;PEN-BLACK;10",
            "SELL;bob;STAPLER;5",
            "CANCEL;O1002",
            "COUNT;STAPLER",
            "SELL;carol;STAPLER;2",
            "SELL;dan;NOTE-A5;14",
            "COUNT;NOTE-A5",
            "DUMP"
        );

        for (String command : commands) {
            processLine(command);
        }
        printEndOfDayReport();
    }

    public void processLine(String line) {
        Command command = commandFactory.createCommand(line);
        command.execute(context);
    }

    public WarehouseContext getContext() {
        return context;
    }

    public void printEndOfDayReport() {
        int shipped = 0;
        int backorder = 0;
        int cancelled = 0;
        for (String status : context.getOrderStatus().values()) {
            if ("SHIPPED".equals(status)) {
                shipped = shipped + 1;
            } else if ("BACKORDER".equals(status)) {
                backorder = backorder + 1;
            } else if (status.startsWith("CANCELLED")) {
                cancelled = cancelled + 1;
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
