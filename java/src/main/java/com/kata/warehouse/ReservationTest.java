package com.kata.warehouse;

public class ReservationTest {
    public static void main(String[] args) {
        WarehouseDeskApp app = new WarehouseDeskApp();
        app.seedData();
        
        System.out.println("=== Testing Reservation Features ===\n");
        
        app.processLine("COUNT;PEN-BLACK");
        
        app.processLine("RESERVE;alice;PEN-BLACK;10;5");
        
        app.processLine("COUNT;PEN-BLACK");
        
        app.processLine("RESERVE;bob;PEN-BLACK;35;10");
        
        app.processLine("CONFIRM;R2001");
        
        app.processLine("COUNT;PEN-BLACK");
        
        app.processLine("RESERVE;carol;PEN-BLUE;5;1");
        
        app.processLine("RELEASE;R2003");
        
        app.processLine("COUNT;PEN-BLUE");
        
        app.processLine("RESERVE;dan;STAPLER;2;0");
        
        System.out.println("\n--- Waiting 2 seconds for expiry ---\n");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        app.processLine("RESERVE;eve;NOTE-A5;1;5");
        
        app.processLine("COUNT;STAPLER");
        
        app.printEndOfDayReport();
    }
}
