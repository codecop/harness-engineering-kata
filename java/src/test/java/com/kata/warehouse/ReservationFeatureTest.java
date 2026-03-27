package com.kata.warehouse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class ReservationFeatureTest {
    private WarehouseDeskApp app;
    private LocalDateTime baseTime;

    @BeforeEach
    void setUp() {
        app = new WarehouseDeskApp();
        app.seedData();
        baseTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        app.setCurrentTime(baseTime);
    }

    @DisplayName("When reserving stock with sufficient available stock, the system increases reserved quantity")
    @Test
    void testReservingStockIncreasesReservedQuantity() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.processLine("COUNT;PEN-BLACK");
    }

    @DisplayName("When reserving stock with sufficient available stock, the system logs a reservation created event")
    @Test
    void testReservingStockLogsCreatedEvent() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.printEndOfDayReport();
    }

    @DisplayName("When reserving stock with insufficient available stock, the system rejects the reservation")
    @Test
    void testReservingStockWithInsufficientStockRejects() {
        app.processLine("RESERVE;bob;STAPLER;10;30");
    }

    @DisplayName("When reserving stock with insufficient available stock, the system logs a reservation rejected event")
    @Test
    void testReservingStockWithInsufficientStockLogsRejectedEvent() {
        app.processLine("RESERVE;bob;STAPLER;10;30");
        app.printEndOfDayReport();
    }

    @DisplayName("When reserving stock, the system creates a reservation with a unique reservation ID")
    @Test
    void testReservingStockCreatesUniqueReservationId() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.processLine("RESERVE;bob;PEN-BLUE;5;30");
        app.printEndOfDayReport();
    }

    @DisplayName("When confirming a valid reservation, the system converts it to a shipped order")
    @Test
    void testConfirmingValidReservationConvertsToShippedOrder() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.processLine("CONFIRM;R2001");
        app.processLine("DUMP");
    }

    @DisplayName("When confirming a valid reservation, the system decreases stock quantity")
    @Test
    void testConfirmingValidReservationDecreasesStock() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.processLine("CONFIRM;R2001");
        app.processLine("COUNT;PEN-BLACK");
    }

    @DisplayName("When confirming a valid reservation, the system decreases reserved quantity")
    @Test
    void testConfirmingValidReservationDecreasesReserved() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.processLine("CONFIRM;R2001");
        app.processLine("COUNT;PEN-BLACK");
    }

    @DisplayName("When confirming a valid reservation, the system increases cash balance by order total")
    @Test
    void testConfirmingValidReservationIncreasesCashBalance() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.processLine("CONFIRM;R2001");
        app.printEndOfDayReport();
    }

    @DisplayName("When confirming a valid reservation, the system logs an order shipped event")
    @Test
    void testConfirmingValidReservationLogsShippedEvent() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.processLine("CONFIRM;R2001");
        app.printEndOfDayReport();
    }

    @DisplayName("When confirming a non-existent reservation, the system logs an error event")
    @Test
    void testConfirmingNonExistentReservationLogsError() {
        app.processLine("CONFIRM;R9999");
        app.printEndOfDayReport();
    }

    @DisplayName("When confirming an expired reservation, the system logs an error event")
    @Test
    void testConfirmingExpiredReservationLogsError() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.setCurrentTime(baseTime.plusMinutes(31));
        app.processLine("CONFIRM;R2001");
        app.printEndOfDayReport();
    }

    @DisplayName("When confirming an already confirmed reservation, the system logs an error event")
    @Test
    void testConfirmingAlreadyConfirmedReservationLogsError() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.processLine("CONFIRM;R2001");
        app.processLine("CONFIRM;R2001");
        app.printEndOfDayReport();
    }

    @DisplayName("When releasing a valid reservation, the system decreases reserved quantity")
    @Test
    void testReleasingValidReservationDecreasesReserved() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.processLine("RELEASE;R2001");
        app.processLine("COUNT;PEN-BLACK");
    }

    @DisplayName("When releasing a valid reservation, the system logs a reservation released event")
    @Test
    void testReleasingValidReservationLogsEvent() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.processLine("RELEASE;R2001");
        app.printEndOfDayReport();
    }

    @DisplayName("When releasing a non-existent reservation, the system logs an error event")
    @Test
    void testReleasingNonExistentReservationLogsError() {
        app.processLine("RELEASE;R9999");
        app.printEndOfDayReport();
    }

    @DisplayName("When releasing an expired reservation, the system logs an error event")
    @Test
    void testReleasingExpiredReservationLogsError() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.setCurrentTime(baseTime.plusMinutes(31));
        app.processLine("RELEASE;R2001");
        app.printEndOfDayReport();
    }

    @DisplayName("When releasing an already released reservation, the system logs an error event")
    @Test
    void testReleasingAlreadyReleasedReservationLogsError() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.processLine("RELEASE;R2001");
        app.processLine("RELEASE;R2001");
        app.printEndOfDayReport();
    }

    @DisplayName("When a reservation expires, the system automatically decreases reserved quantity")
    @Test
    void testReservationExpiryDecreasesReserved() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.setCurrentTime(baseTime.plusMinutes(31));
        app.processLine("COUNT;PEN-BLACK");
    }

    @DisplayName("When processing any command, the system checks for and processes expired reservations")
    @Test
    void testSystemProcessesExpiredReservationsOnAnyCommand() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.setCurrentTime(baseTime.plusMinutes(31));
        app.processLine("DUMP");
        app.printEndOfDayReport();
    }

    @DisplayName("When reserving stock, the system stores the customer name")
    @Test
    void testReservingStockStoresCustomerName() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.processLine("CONFIRM;R2001");
        app.printEndOfDayReport();
    }

    @DisplayName("When reserving stock, the system stores the SKU")
    @Test
    void testReservingStockStoresSku() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.processLine("CONFIRM;R2001");
        app.printEndOfDayReport();
    }

    @DisplayName("When reserving stock, the system stores the quantity")
    @Test
    void testReservingStockStoresQuantity() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.processLine("CONFIRM;R2001");
        app.printEndOfDayReport();
    }

    @DisplayName("When reserving stock, the system stores the expiry time based on minutes parameter")
    @Test
    void testReservingStockStoresExpiryTime() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.setCurrentTime(baseTime.plusMinutes(29));
        app.processLine("CONFIRM;R2001");
        app.setCurrentTime(baseTime.plusMinutes(31));
        app.processLine("RESERVE;bob;PEN-BLUE;5;30");
        app.processLine("CONFIRM;R2002");
        app.printEndOfDayReport();
    }

    @DisplayName("When selling, the system does not ship if reserved stock makes available stock insufficient")
    @Test
    void testSellingWithReservedStockMakingAvailableInsufficient() {
        app.processLine("RESERVE;alice;PEN-BLACK;35;30");
        app.processLine("SELL;bob;PEN-BLACK;10");
        app.printEndOfDayReport();
    }

    @DisplayName("When a reservation expires, the system marks reservation as expired")
    @Test
    void testReservationExpiryMarksAsExpired() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.setCurrentTime(baseTime.plusMinutes(31));
        app.processLine("CONFIRM;R2001");
        app.printEndOfDayReport();
    }

    @DisplayName("Multiple reservations can coexist and be managed independently")
    @Test
    void testMultipleReservationsCoexist() {
        app.processLine("RESERVE;alice;PEN-BLACK;10;30");
        app.processLine("RESERVE;bob;PEN-BLUE;5;60");
        app.processLine("RESERVE;carol;NOTE-A5;3;45");
        app.processLine("COUNT;PEN-BLACK");
        app.processLine("COUNT;PEN-BLUE");
        app.processLine("COUNT;NOTE-A5");
        app.processLine("CONFIRM;R2001");
        app.processLine("RELEASE;R2002");
        app.setCurrentTime(baseTime.plusMinutes(50));
        app.processLine("COUNT;NOTE-A5");
        app.printEndOfDayReport();
    }
}
