package com.kata.warehouse;

public class UnknownCommand implements Command {
    private final String line;

    public UnknownCommand(String line) {
        this.line = line;
    }

    @Override
    public void execute(WarehouseContext context) {
        context.getEventLog().add("unknown command: " + line);
    }
}
