package com.kata.warehouse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseDeskAppTest {
    private WarehouseDeskApp app;
    private FakeTimeProvider timeProvider;

    @BeforeEach
    void setUp() {
        timeProvider = new FakeTimeProvider(1000000L);
        app = new WarehouseDeskApp(timeProvider);
        app.seedData();
    }

    @Test
    void testReceiveCommand() {
        app.processLine("RECV;NOTE-A5;5;2.20");
        assertEquals(20, app.getContext().getStockBySku().get("NOTE-A5"));
        assertEquals(289.0, app.getContext().getCashBalance(), 0.01);
        assertTrue(app.getContext().getEventLog().get(0).contains("received 5 of NOTE-A5 at 2.2"));
    }

    @Test
    void testSellCommandSuccess() {
        app.processLine("SELL;alice;PEN-BLACK;10");
        assertEquals(30, app.getContext().getStockBySku().get("PEN-BLACK"));
        assertEquals(315.0, app.getContext().getCashBalance(), 0.01);
        assertEquals("SHIPPED", app.getContext().getOrderStatus().get("O1001"));
    }

    @Test
    void testSellCommandBackorder() {
        app.processLine("SELL;bob;STAPLER;10");
        assertEquals(4, app.getContext().getStockBySku().get("STAPLER"));
        assertEquals("BACKORDER", app.getContext().getOrderStatus().get("O1001"));
    }

    @Test
    void testCancelBackorder() {
        app.processLine("SELL;bob;STAPLER;10");
        app.processLine("CANCEL;O1001");
        assertEquals("CANCELLED", app.getContext().getOrderStatus().get("O1001"));
    }

    @Test
    void testCancelShippedOrder() {
        app.processLine("SELL;alice;PEN-BLACK;10");
        app.processLine("CANCEL;O1001");
        assertEquals(40, app.getContext().getStockBySku().get("PEN-BLACK"));
        assertEquals(300.0, app.getContext().getCashBalance(), 0.01);
        assertEquals("CANCELLED_AFTER_SHIP", app.getContext().getOrderStatus().get("O1001"));
    }

    @Test
    void testCountCommand() {
        app.processLine("COUNT;STAPLER");
        String lastEvent = app.getContext().getEventLog().get(0);
        assertTrue(lastEvent.contains("count STAPLER onHand=4 reserved=0 available=4"));
    }

    @Test
    void testDemoDay() {
        app.runDemoDay();
        assertTrue(app.getContext().getEventLog().size() > 0);
    }
}
