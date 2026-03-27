package com.kata.warehouse.command;

import com.kata.warehouse.WarehouseDeskApp;

public interface CommandProcessor {
    void process(String[] parts, WarehouseDeskApp app);
    String getCommandType();
}
