package com.kata.warehouse;

public interface Command {
    void execute(WarehouseContext context, String[] parts);
}
