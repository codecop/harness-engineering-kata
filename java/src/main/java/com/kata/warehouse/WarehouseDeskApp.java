package com.kata.warehouse;

import com.kata.warehouse.shared.Money;
import com.kata.warehouse.shared.Quantity;
import com.kata.warehouse.shared.ReportGenerator;
import com.kata.warehouse.shared.SKU;

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
        context.getInventoryService().receiveStock(new SKU("PEN-BLACK"), new Quantity(40), new Money(1.5));
        context.getInventoryService().receiveStock(new SKU("PEN-BLUE"), new Quantity(25), new Money(1.6));
        context.getInventoryService().receiveStock(new SKU("NOTE-A5"), new Quantity(15), new Money(4.0));
        context.getInventoryService().receiveStock(new SKU("STAPLER"), new Quantity(4), new Money(12.0));

        context.getCashService().addCash(new Money(300.0));
        context.getOrderService().setNextOrderNumber(1001);
        context.getReservationService().setNextReservationNumber(2001);
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
            context.getEventLogService().addEvent("unknown command: " + line);
        }
    }

    public void printEndOfDayReport() {
        reportGenerator.printEndOfDayReport(context);
    }

    WarehouseContext getContext() {
        return context;
    }
}
