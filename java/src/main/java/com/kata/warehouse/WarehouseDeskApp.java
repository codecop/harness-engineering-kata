package com.kata.warehouse;

import java.util.List;

public class WarehouseDeskApp {
    private final WarehouseContext context;
    private final CommandFactory commandFactory;
    private final ExpiryChecker expiryChecker;
    private final ReportGenerator reportGenerator;

    public WarehouseDeskApp() {
        this.context = new WarehouseContext();
        this.commandFactory = new CommandFactory();
        this.expiryChecker = new ExpiryChecker();
        this.reportGenerator = new ReportGenerator();
    }

    public void seedData() {
        context.getStockBySku().put("PEN-BLACK", 40);
        context.getStockBySku().put("PEN-BLUE", 25);
        context.getStockBySku().put("NOTE-A5", 15);
        context.getStockBySku().put("STAPLER", 4);

        context.getReservedBySku().put("PEN-BLACK", 0);
        context.getReservedBySku().put("PEN-BLUE", 0);
        context.getReservedBySku().put("NOTE-A5", 0);
        context.getReservedBySku().put("STAPLER", 0);

        context.getPriceBySku().put("PEN-BLACK", 1.5);
        context.getPriceBySku().put("PEN-BLUE", 1.6);
        context.getPriceBySku().put("NOTE-A5", 4.0);
        context.getPriceBySku().put("STAPLER", 12.0);

        context.setCashBalance(300.0);
        context.setNextOrderNumber(1001);
        context.setNextReservationNumber(2001);
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
        expiryChecker.checkExpiredReservations(context);
        String[] parts = line.split(";");
        String type = parts[0];

        Command command = commandFactory.getCommand(type);
        if (command != null) {
            command.execute(context, parts);
        } else {
            context.addEvent("unknown command: " + line);
        }
    }

    public void printEndOfDayReport() {
        reportGenerator.printEndOfDayReport(context);
    }

    WarehouseContext getContext() {
        return context;
    }
}
