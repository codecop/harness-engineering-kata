package com.kata.warehouse;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        WarehouseDeskApp app = new WarehouseDeskApp();
        app.seedData();
        app.runDemoDay();
    }
}
