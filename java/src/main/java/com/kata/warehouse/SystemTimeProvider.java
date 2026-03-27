package com.kata.warehouse;

public class SystemTimeProvider implements TimeProvider {
    @Override
    public long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }
}
