package com.kata.warehouse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {
    private WarehouseDeskApp app;
    private FakeTimeProvider timeProvider;

    @BeforeEach
    void setUp() {
        timeProvider = new FakeTimeProvider(1000000L);
        app = new WarehouseDeskApp(timeProvider);
        app.seedData();
    }

    @Test
    void testReserveSuccess() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;5");
        assertEquals(10, app.getContext().getReservedBySku().get("PEN-BLACK"));
        assertEquals(1, app.getContext().getReservations().size());
        assertTrue(app.getContext().getEventLog().get(0).contains("reservation R1 created"));
    }

    @Test
    void testReserveInsufficientStock() {
        app.processLine("RESERVE;bob;STAPLER;10;5");
        assertEquals(0, app.getContext().getReservedBySku().get("STAPLER"));
        assertEquals(0, app.getContext().getReservations().size());
        assertTrue(app.getContext().getEventLog().get(0).contains("reservation failed"));
    }

    @Test
    void testReserveReducesAvailableStock() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;5");
        app.processLine("SELL;bob;PEN-BLACK;35");
        assertEquals(40, app.getContext().getStockBySku().get("PEN-BLACK"));
        assertEquals(10, app.getContext().getReservedBySku().get("PEN-BLACK"));
        assertEquals("BACKORDER", app.getContext().getOrderStatus().get("O1001"));
    }

    @Test
    void testConfirmReservation() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;5");
        app.processLine("CONFIRM;R1");
        assertEquals(30, app.getContext().getStockBySku().get("PEN-BLACK"));
        assertEquals(0, app.getContext().getReservedBySku().get("PEN-BLACK"));
        assertEquals("SHIPPED", app.getContext().getOrderStatus().get("O1001"));
        assertEquals(315.0, app.getContext().getCashBalance(), 0.01);
    }

    @Test
    void testConfirmNonExistentReservation() {
        app.processLine("CONFIRM;R999");
        assertTrue(app.getContext().getEventLog().get(0).contains("cannot confirm R999"));
    }

    @Test
    void testReleaseReservation() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;5");
        app.processLine("RELEASE;R1");
        assertEquals(0, app.getContext().getReservedBySku().get("PEN-BLACK"));
        assertTrue(app.getContext().getEventLog().get(1).contains("reservation R1 released"));
    }

    @Test
    void testReleaseNonExistentReservation() {
        app.processLine("RELEASE;R999");
        assertTrue(app.getContext().getEventLog().get(0).contains("cannot release R999"));
    }

    @Test
    void testReservationExpiry() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;5");
        assertEquals(10, app.getContext().getReservedBySku().get("PEN-BLACK"));
        
        timeProvider.advanceMinutes(6);
        app.processLine("COUNT;PEN-BLACK");
        
        assertEquals(0, app.getContext().getReservedBySku().get("PEN-BLACK"));
        assertTrue(app.getContext().getEventLog().stream()
            .anyMatch(e -> e.contains("reservation R1 expired")));
    }

    @Test
    void testConfirmExpiredReservation() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;5");
        timeProvider.advanceMinutes(6);
        app.processLine("CONFIRM;R1");
        
        assertTrue(app.getContext().getEventLog().stream()
            .anyMatch(e -> e.contains("cannot confirm R1 because it is no longer active")));
    }

    @Test
    void testReleaseExpiredReservation() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;5");
        timeProvider.advanceMinutes(6);
        app.processLine("COUNT;PEN-BLACK");
        app.processLine("RELEASE;R1");
        
        assertTrue(app.getContext().getEventLog().stream()
            .anyMatch(e -> e.contains("cannot release R1 because it is no longer active")));
    }

    @Test
    void testMultipleReservations() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;5");
        app.processLine("RESERVE;bob;PEN-BLACK;15;10");
        assertEquals(25, app.getContext().getReservedBySku().get("PEN-BLACK"));
        assertEquals(2, app.getContext().getReservations().size());
    }

    @Test
    void testReservationExpiryRestoresStock() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;5");
        timeProvider.advanceMinutes(6);
        app.processLine("SELL;bob;PEN-BLACK;40");
        
        assertEquals(0, app.getContext().getStockBySku().get("PEN-BLACK"));
        assertEquals("SHIPPED", app.getContext().getOrderStatus().get("O1001"));
    }
}
