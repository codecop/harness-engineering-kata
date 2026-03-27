package com.kata.warehouse;

public class FakeTimeProvider implements TimeProvider {
    private long currentTimeMillis;

    public FakeTimeProvider(long initialTimeMillis) {
        this.currentTimeMillis = initialTimeMillis;
    }

    @Override
    public long getCurrentTimeMillis() {
        return currentTimeMillis;
    }

    public void advanceMinutes(int minutes) {
        currentTimeMillis += minutes * 60L * 1000L;
    }

    public void setCurrentTimeMillis(long timeMillis) {
        this.currentTimeMillis = timeMillis;
    }
}
