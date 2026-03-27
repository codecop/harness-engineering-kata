package com.kata.warehouse.command;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private final Map<String, CommandProcessor> processors = new HashMap<>();

    public void register(CommandProcessor processor) {
        processors.put(processor.getCommandType(), processor);
    }

    public CommandProcessor get(String commandType) {
        return processors.get(commandType);
    }
}
