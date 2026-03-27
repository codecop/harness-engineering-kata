package com.kata.warehouse;

public class DumpCommand implements Command {
    @Override
    public void execute(WarehouseContext context) {
        System.out.println("---- dump ----");
        System.out.println("stock=" + context.getStockBySku());
        System.out.println("reserved=" + context.getReservedBySku());
        System.out.println("orders=" + context.getOrderStatus());
        System.out.println("reservations=" + context.getReservations().size() + " active");
        System.out.println("cashBalance=" + context.getCashBalance());
    }
}
