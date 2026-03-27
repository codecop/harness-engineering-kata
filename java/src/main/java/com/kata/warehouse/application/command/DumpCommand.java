package com.kata.warehouse.application.command;

import com.kata.warehouse.WarehouseContext;

public class DumpCommand implements Command {
    @Override
    public void execute(WarehouseContext context, String[] parts) {
        System.out.println("Cash balance: " + context.getCashService().getCashBalance());
    }
}
