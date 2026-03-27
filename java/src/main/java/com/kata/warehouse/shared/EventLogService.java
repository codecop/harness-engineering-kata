package com.kata.warehouse.shared;

import java.util.ArrayList;
import java.util.List;

public class EventLogService {
    private final List<String> eventLog = new ArrayList<>();

    public void addEvent(String event) {
        eventLog.add(event);
    }

    public List<String> getEventLog() {
        return new ArrayList<>(eventLog);
    }
}
