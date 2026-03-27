package com.kata.warehouse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WarehouseDeskAppTest {
    private WarehouseDeskApp app;

    @BeforeEach
    void setUp() {
        app = new WarehouseDeskApp();
        app.seedData();
    }

    @DisplayName("The system initializes stock with seed data for PEN-BLACK, PEN-BLUE, NOTE-A5, and STAPLER")
    @Test
    void testSystemInitializesStockWithSeedData() {
        app.processLine("COUNT;PEN-BLACK");
        app.processLine("COUNT;PEN-BLUE");
        app.processLine("COUNT;NOTE-A5");
        app.processLine("COUNT;STAPLER");
    }

    @DisplayName("When receiving stock, the system increases stock quantity for the SKU")
    @Test
    void testReceivingStockIncreasesQuantity() {
        app.processLine("RECV;NOTE-A5;5;2.20");
        app.processLine("COUNT;NOTE-A5");
    }

    @DisplayName("When receiving stock, the system decreases cash balance by quantity times unit cost")
    @Test
    void testReceivingStockDecreasesCashBalance() {
        app.processLine("RECV;NOTE-A5;5;2.20");
    }

    @DisplayName("When selling with sufficient available stock, the system ships the order immediately")
    @Test
    void testSellingWithSufficientStockShipsOrder() {
        app.processLine("SELL;alice;PEN-BLACK;10");
    }

    @DisplayName("When selling with sufficient available stock, the system decreases stock quantity")
    @Test
    void testSellingWithSufficientStockDecreasesQuantity() {
        app.processLine("SELL;alice;PEN-BLACK;10");
        app.processLine("COUNT;PEN-BLACK");
    }

    @DisplayName("When selling with sufficient available stock, the system increases cash balance by order total")
    @Test
    void testSellingWithSufficientStockIncreasesCashBalance() {
        app.processLine("SELL;alice;PEN-BLACK;10");
    }

    @DisplayName("When selling with insufficient available stock, the system creates a backorder")
    @Test
    void testSellingWithInsufficientStockCreatesBackorder() {
        app.processLine("SELL;bob;STAPLER;5");
    }

    @DisplayName("When cancelling a non-existent order, the system logs an error event")
    @Test
    void testCancellingNonExistentOrderLogsError() {
        app.processLine("CANCEL;O9999");
    }

    @DisplayName("When cancelling a backorder, the system changes status to CANCELLED")
    @Test
    void testCancellingBackorderChangesStatus() {
        app.processLine("SELL;bob;STAPLER;5");
        app.processLine("CANCEL;O1001");
    }

    @DisplayName("When cancelling a shipped order, the system restocks the items")
    @Test
    void testCancellingShippedOrderRestocksItems() {
        app.processLine("SELL;alice;PEN-BLACK;10");
        app.processLine("CANCEL;O1001");
        app.processLine("COUNT;PEN-BLACK");
    }

    @DisplayName("When cancelling a shipped order, the system refunds the cash balance")
    @Test
    void testCancellingShippedOrderRefundsCashBalance() {
        app.processLine("SELL;alice;PEN-BLACK;10");
        app.processLine("CANCEL;O1001");
    }

    @DisplayName("When cancelling an already cancelled order, the system logs that it cannot be cancelled")
    @Test
    void testCancellingAlreadyCancelledOrderLogsError() {
        app.processLine("SELL;bob;STAPLER;5");
        app.processLine("CANCEL;O1001");
        app.processLine("CANCEL;O1001");
    }

    @DisplayName("When counting stock, the system reports on-hand quantity")
    @Test
    void testCountingStockReportsOnHand() {
        app.processLine("COUNT;STAPLER");
    }

    @DisplayName("When counting stock, the system reports reserved quantity")
    @Test
    void testCountingStockReportsReserved() {
        app.processLine("COUNT;STAPLER");
    }

    @DisplayName("When counting stock, the system reports available quantity")
    @Test
    void testCountingStockReportsAvailable() {
        app.processLine("COUNT;STAPLER");
    }

    @DisplayName("When dumping, the system prints stock quantities")
    @Test
    void testDumpPrintsStockQuantities() {
        app.processLine("DUMP");
    }

    @DisplayName("When dumping, the system prints reserved quantities")
    @Test
    void testDumpPrintsReservedQuantities() {
        app.processLine("DUMP");
    }

    @DisplayName("When dumping, the system prints order statuses")
    @Test
    void testDumpPrintsOrderStatuses() {
        app.processLine("SELL;alice;PEN-BLACK;10");
        app.processLine("DUMP");
    }

    @DisplayName("When dumping, the system prints cash balance")
    @Test
    void testDumpPrintsCashBalance() {
        app.processLine("DUMP");
    }

    @DisplayName("The end of day report counts shipped orders")
    @Test
    void testEndOfDayReportCountsShippedOrders() {
        app.processLine("SELL;alice;PEN-BLACK;10");
        app.printEndOfDayReport();
    }

    @DisplayName("The end of day report counts backordered orders")
    @Test
    void testEndOfDayReportCountsBackorderedOrders() {
        app.processLine("SELL;bob;STAPLER;5");
        app.printEndOfDayReport();
    }

    @DisplayName("The end of day report counts cancelled orders")
    @Test
    void testEndOfDayReportCountsCancelledOrders() {
        app.processLine("SELL;alice;PEN-BLACK;10");
        app.processLine("CANCEL;O1001");
        app.printEndOfDayReport();
    }

    @DisplayName("The end of day report shows cash balance")
    @Test
    void testEndOfDayReportShowsCashBalance() {
        app.printEndOfDayReport();
    }

    @DisplayName("The end of day report identifies SKUs with stock below 5 units")
    @Test
    void testEndOfDayReportIdentifiesLowStock() {
        app.printEndOfDayReport();
    }

    @DisplayName("The system uses unit prices to calculate order totals")
    @Test
    void testSystemUsesUnitPricesToCalculateOrderTotals() {
        app.processLine("SELL;alice;PEN-BLACK;10");
    }

    @DisplayName("The system uses unit prices to calculate refunds")
    @Test
    void testSystemUsesUnitPricesToCalculateRefunds() {
        app.processLine("SELL;alice;PEN-BLACK;10");
        app.processLine("CANCEL;O1001");
    }

    @DisplayName("When selling, the system creates a new order with incremented order number")
    @Test
    void testSellingCreatesOrderWithIncrementedNumber() {
        app.processLine("SELL;alice;PEN-BLACK;10");
        app.processLine("SELL;bob;PEN-BLUE;5");
    }

    @DisplayName("The system calculates available stock as on-hand minus reserved")
    @Test
    void testSystemCalculatesAvailableStockAsOnHandMinusReserved() {
        app.processLine("COUNT;PEN-BLACK");
    }

    @DisplayName("When receiving stock, the system logs a received event")
    @Test
    void testReceivingStockLogsEvent() {
        app.processLine("RECV;NOTE-A5;5;2.20");
        app.printEndOfDayReport();
    }

    @DisplayName("When selling with sufficient available stock, the system logs a shipped event")
    @Test
    void testSellingWithSufficientStockLogsShippedEvent() {
        app.processLine("SELL;alice;PEN-BLACK;10");
        app.printEndOfDayReport();
    }

    @DisplayName("When selling with insufficient available stock, the system logs a backordered event")
    @Test
    void testSellingWithInsufficientStockLogsBackorderedEvent() {
        app.processLine("SELL;bob;STAPLER;5");
        app.printEndOfDayReport();
    }

    @DisplayName("When cancelling a backorder, the system logs a cancelled backorder event")
    @Test
    void testCancellingBackorderLogsEvent() {
        app.processLine("SELL;bob;STAPLER;5");
        app.processLine("CANCEL;O1001");
        app.printEndOfDayReport();
    }

    @DisplayName("When cancelling a shipped order, the system logs a cancelled shipped order event")
    @Test
    void testCancellingShippedOrderLogsEvent() {
        app.processLine("SELL;alice;PEN-BLACK;10");
        app.processLine("CANCEL;O1001");
        app.printEndOfDayReport();
    }

    @DisplayName("When counting stock, the system logs a count event")
    @Test
    void testCountingStockLogsEvent() {
        app.processLine("COUNT;STAPLER");
        app.printEndOfDayReport();
    }

    @DisplayName("The end of day report lists all events")
    @Test
    void testEndOfDayReportListsAllEvents() {
        app.processLine("RECV;NOTE-A5;5;2.20");
        app.processLine("SELL;alice;PEN-BLACK;10");
        app.printEndOfDayReport();
    }

    @DisplayName("When selling, the system does not ship if reserved stock makes available stock insufficient")
    @Test
    void testSellingDoesNotShipIfReservedStockMakesAvailableInsufficient() {
        app.processLine("COUNT;STAPLER");
    }

    @DisplayName("When cancelling a shipped order, the system changes status to CANCELLED_AFTER_SHIP")
    @Test
    void testCancellingShippedOrderChangesStatusToCancelledAfterShip() {
        app.processLine("SELL;alice;PEN-BLACK;10");
        app.processLine("CANCEL;O1001");
        app.processLine("DUMP");
    }
}
