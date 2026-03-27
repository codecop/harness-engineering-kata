package com.kata.warehouse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseDeskAppTest {
    private WarehouseDeskApp app;

    @BeforeEach
    void setUp() {
        app = new WarehouseDeskApp();
        app.seedData();
    }

    private int getStockBySku(String sku) throws Exception {
        WarehouseContext context = app.getContext();
        return context.getStockBySku().getOrDefault(sku, 0);
    }

    private int getReservedBySku(String sku) throws Exception {
        WarehouseContext context = app.getContext();
        return context.getReservedBySku().getOrDefault(sku, 0);
    }

    private double getCashBalance() throws Exception {
        WarehouseContext context = app.getContext();
        return context.getCashBalance();
    }

    private String getOrderStatus(String orderId) throws Exception {
        WarehouseContext context = app.getContext();
        return context.getOrderStatus().get(orderId);
    }

    @Test
    @DisplayName("Receiving stock increases the on-hand quantity for a SKU")
    void receivingStockIncreasesOnHandQuantity() throws Exception {
        int initialStock = getStockBySku("PEN-BLACK");
        app.processLine("RECV;PEN-BLACK;10;1.0");
        int finalStock = getStockBySku("PEN-BLACK");
        assertEquals(initialStock + 10, finalStock);
    }

    @Test
    @DisplayName("Receiving stock decreases cash balance by the total cost")
    void receivingStockDecreasesCashBalance() throws Exception {
        double initialCash = getCashBalance();
        app.processLine("RECV;PEN-BLACK;10;1.5");
        double finalCash = getCashBalance();
        assertEquals(initialCash - 15.0, finalCash, 0.001);
    }

    @Test
    @DisplayName("Receiving stock for a new SKU creates that SKU in the system")
    void receivingStockForNewSkuCreatesIt() throws Exception {
        app.processLine("RECV;NEW-ITEM;5;2.0");
        int stock = getStockBySku("NEW-ITEM");
        assertEquals(5, stock);
    }

    @Test
    @DisplayName("Selling with sufficient available stock ships the order immediately")
    void sellingWithSufficientStockShipsImmediately() throws Exception {
        app.processLine("SELL;alice;PEN-BLACK;5");
        String status = getOrderStatus("O1001");
        assertEquals("SHIPPED", status);
    }

    @Test
    @DisplayName("Selling with insufficient available stock creates a backorder")
    void sellingWithInsufficientStockCreatesBackorder() throws Exception {
        app.processLine("SELL;bob;PEN-BLACK;50");
        String status = getOrderStatus("O1001");
        assertEquals("BACKORDER", status);
    }

    @Test
    @DisplayName("Shipping an order decreases stock by the order quantity")
    void shippingOrderDecreasesStock() throws Exception {
        int initialStock = getStockBySku("PEN-BLACK");
        app.processLine("SELL;alice;PEN-BLACK;5");
        int finalStock = getStockBySku("PEN-BLACK");
        assertEquals(initialStock - 5, finalStock);
    }

    @Test
    @DisplayName("Shipping an order increases cash balance by the total price")
    void shippingOrderIncreasesCashBalance() throws Exception {
        double initialCash = getCashBalance();
        app.processLine("SELL;alice;PEN-BLACK;10");
        double finalCash = getCashBalance();
        assertEquals(initialCash + 15.0, finalCash, 0.001);
    }

    @Test
    @DisplayName("Backordering an order does not change stock")
    void backorderingOrderDoesNotChangeStock() throws Exception {
        int initialStock = getStockBySku("PEN-BLACK");
        app.processLine("SELL;bob;PEN-BLACK;50");
        int finalStock = getStockBySku("PEN-BLACK");
        assertEquals(initialStock, finalStock);
    }

    @Test
    @DisplayName("Available stock is calculated as on-hand stock minus reserved stock")
    void availableStockCalculation() throws Exception {
        int onHand = getStockBySku("STAPLER");
        int reserved = getReservedBySku("STAPLER");
        int available = onHand - reserved;
        
        app.processLine("SELL;alice;STAPLER;" + available);
        String status = getOrderStatus("O1001");
        assertEquals("SHIPPED", status);
        
        app.processLine("SELL;bob;STAPLER;1");
        String status2 = getOrderStatus("O1002");
        assertEquals("BACKORDER", status2);
    }

    @Test
    @DisplayName("Cancelling a backordered order changes its status to CANCELLED")
    void cancellingBackorderedOrder() throws Exception {
        app.processLine("SELL;alice;PEN-BLACK;50");
        app.processLine("CANCEL;O1001");
        String status = getOrderStatus("O1001");
        assertEquals("CANCELLED", status);
    }

    @Test
    @DisplayName("Cancelling a shipped order restocks the items")
    void cancellingShippedOrderRestocksItems() throws Exception {
        int initialStock = getStockBySku("PEN-BLACK");
        app.processLine("SELL;alice;PEN-BLACK;10");
        app.processLine("CANCEL;O1001");
        int finalStock = getStockBySku("PEN-BLACK");
        assertEquals(initialStock, finalStock);
    }

    @Test
    @DisplayName("Cancelling a shipped order refunds the cash balance")
    void cancellingShippedOrderRefundsCash() throws Exception {
        double initialCash = getCashBalance();
        app.processLine("SELL;alice;PEN-BLACK;10");
        app.processLine("CANCEL;O1001");
        double finalCash = getCashBalance();
        assertEquals(initialCash, finalCash, 0.001);
    }

    @Test
    @DisplayName("Cancelling a shipped order changes its status to CANCELLED_AFTER_SHIP")
    void cancellingShippedOrderChangesStatus() throws Exception {
        app.processLine("SELL;alice;PEN-BLACK;10");
        app.processLine("CANCEL;O1001");
        String status = getOrderStatus("O1001");
        assertEquals("CANCELLED_AFTER_SHIP", status);
    }

    @Test
    @DisplayName("System initializes with predefined SKUs and stock levels")
    void systemInitializesWithPredefinedStock() throws Exception {
        assertEquals(40, getStockBySku("PEN-BLACK"));
        assertEquals(25, getStockBySku("PEN-BLUE"));
        assertEquals(15, getStockBySku("NOTE-A5"));
        assertEquals(4, getStockBySku("STAPLER"));
    }

    @Test
    @DisplayName("System initializes reserved quantities to zero for all SKUs")
    void systemInitializesReservedToZero() throws Exception {
        assertEquals(0, getReservedBySku("PEN-BLACK"));
        assertEquals(0, getReservedBySku("PEN-BLUE"));
        assertEquals(0, getReservedBySku("NOTE-A5"));
        assertEquals(0, getReservedBySku("STAPLER"));
    }

    @Test
    @DisplayName("System initializes with a starting cash balance")
    void systemInitializesWithCashBalance() throws Exception {
        assertEquals(300.0, getCashBalance(), 0.001);
    }

    @Test
    @DisplayName("Selling creates a new order with a unique order ID")
    void sellingCreatesUniqueOrderId() throws Exception {
        app.processLine("SELL;alice;PEN-BLACK;1");
        app.processLine("SELL;bob;PEN-BLUE;1");
        assertNotNull(getOrderStatus("O1001"));
        assertNotNull(getOrderStatus("O1002"));
    }

    @Test
    @DisplayName("Demo scenario executes without errors")
    void demoScenarioExecutesWithoutErrors() {
        assertDoesNotThrow(() -> {
            app.runDemoDay();
        });
    }
}
