package com.kata.warehouse;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private final Map<String, Command> commands = new HashMap<>();

    public CommandFactory() {
        commands.put("RECV", new ReceiveCommand());
        commands.put("SELL", new SellCommand());
        commands.put("CANCEL", new CancelCommand());
        commands.put("COUNT", new CountCommand());
        commands.put("DUMP", new DumpCommand());
        commands.put("RESERVE", new ReserveCommand());
        commands.put("CONFIRM", new ConfirmCommand());
        commands.put("RELEASE", new ReleaseCommand());
    }

    public Command getCommand(String type) {
        return commands.get(type);
    }
}
