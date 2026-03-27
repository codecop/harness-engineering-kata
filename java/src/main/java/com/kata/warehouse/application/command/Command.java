package com.kata.warehouse.application.command;

import com.kata.warehouse.WarehouseContext;

public interface Command {
    void execute(WarehouseContext context, String[] parts);
}
