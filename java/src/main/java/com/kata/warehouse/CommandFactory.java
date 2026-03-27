package com.kata.warehouse;

import com.kata.warehouse.financial.DumpCommand;
import com.kata.warehouse.inventory.CountCommand;
import com.kata.warehouse.inventory.ReceiveCommand;
import com.kata.warehouse.order.CancelCommand;
import com.kata.warehouse.order.SellCommand;
import com.kata.warehouse.reservation.ConfirmCommand;
import com.kata.warehouse.reservation.ReleaseCommand;
import com.kata.warehouse.reservation.ReserveCommand;

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
