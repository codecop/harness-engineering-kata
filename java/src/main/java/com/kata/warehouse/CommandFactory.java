package com.kata.warehouse;

public class CommandFactory {
    public Command createCommand(String line) {
        String[] parts = line.split(";");
        String type = parts[0];

        if ("RECV".equals(type)) {
            return new ReceiveCommand(parts[1], parseInt(parts[2]), parseDouble(parts[3]));
        }

        if ("SELL".equals(type)) {
            return new SellCommand(parts[1], parts[2], parseInt(parts[3]));
        }

        if ("CANCEL".equals(type)) {
            return new CancelCommand(parts[1]);
        }

        if ("COUNT".equals(type)) {
            return new CountCommand(parts[1]);
        }

        if ("DUMP".equals(type)) {
            return new DumpCommand();
        }

        if ("RESERVE".equals(type)) {
            return new ReserveCommand(parts[1], parts[2], parseInt(parts[3]), parseInt(parts[4]));
        }

        if ("CONFIRM".equals(type)) {
            return new ConfirmCommand(parts[1]);
        }

        if ("RELEASE".equals(type)) {
            return new ReleaseCommand(parts[1]);
        }

        return new UnknownCommand(line);
    }

    private int parseInt(String value) {
        return Integer.parseInt(value.trim());
    }

    private double parseDouble(String value) {
        return Double.parseDouble(value.trim());
    }
}
