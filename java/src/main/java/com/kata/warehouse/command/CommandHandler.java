package com.kata.warehouse.command;

import java.util.List;

public interface CommandHandler {
    void handle(String[] parts);
    List<String> getEventLog();
}
