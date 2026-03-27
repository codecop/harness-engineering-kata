package com.kata.warehouse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {
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

    private Reservation getReservation(String reservationId) throws Exception {
        WarehouseContext context = app.getContext();
        return context.getReservations().get(reservationId);
    }

    @Test
    @DisplayName("Reserving stock creates a reservation with a unique reservation ID")
    void reservingStockCreatesReservationWithUniqueId() throws Exception {
        app.processLine("RESERVE;alice;PEN-BLACK;5;10");
        Reservation reservation = getReservation("R2001");
        assertNotNull(reservation);
        assertEquals("R2001", reservation.getReservationId());
    }

    @Test
    @DisplayName("Reserving stock only succeeds if sufficient available stock exists")
    void reservingStockRequiresSufficientAvailableStock() throws Exception {
        app.processLine("RESERVE;alice;PEN-BLACK;50;10");
        Reservation reservation = getReservation("R2001");
        assertNull(reservation);
    }

    @Test
    @DisplayName("Reserving stock increases the reserved quantity for the SKU")
    void reservingStockIncreasesReservedQuantity() throws Exception {
        int initialReserved = getReservedBySku("PEN-BLACK");
        app.processLine("RESERVE;alice;PEN-BLACK;5;10");
        int finalReserved = getReservedBySku("PEN-BLACK");
        assertEquals(initialReserved + 5, finalReserved);
    }

    @Test
    @DisplayName("Reserving stock does not change the on-hand quantity")
    void reservingStockDoesNotChangeOnHandQuantity() throws Exception {
        int initialStock = getStockBySku("PEN-BLACK");
        app.processLine("RESERVE;alice;PEN-BLACK;5;10");
        int finalStock = getStockBySku("PEN-BLACK");
        assertEquals(initialStock, finalStock);
    }

    @Test
    @DisplayName("Reserving with insufficient available stock fails and logs an error")
    void reservingWithInsufficientStockFails() throws Exception {
        int initialReserved = getReservedBySku("PEN-BLACK");
        app.processLine("RESERVE;alice;PEN-BLACK;50;10");
        int finalReserved = getReservedBySku("PEN-BLACK");
        assertEquals(initialReserved, finalReserved);
    }

    @Test
    @DisplayName("Confirming a reservation converts it into a shipped order")
    void confirmingReservationCreatesShippedOrder() throws Exception {
        app.processLine("RESERVE;alice;PEN-BLACK;5;10");
        app.processLine("CONFIRM;R2001");
        String status = getOrderStatus("O1001");
        assertEquals("SHIPPED", status);
    }

    @Test
    @DisplayName("Confirming a reservation decreases on-hand stock by the quantity")
    void confirmingReservationDecreasesOnHandStock() throws Exception {
        int initialStock = getStockBySku("PEN-BLACK");
        app.processLine("RESERVE;alice;PEN-BLACK;5;10");
        app.processLine("CONFIRM;R2001");
        int finalStock = getStockBySku("PEN-BLACK");
        assertEquals(initialStock - 5, finalStock);
    }

    @Test
    @DisplayName("Confirming a reservation decreases reserved stock by the quantity")
    void confirmingReservationDecreasesReservedStock() throws Exception {
        app.processLine("RESERVE;alice;PEN-BLACK;5;10");
        int reservedAfterReserve = getReservedBySku("PEN-BLACK");
        app.processLine("CONFIRM;R2001");
        int reservedAfterConfirm = getReservedBySku("PEN-BLACK");
        assertEquals(reservedAfterReserve - 5, reservedAfterConfirm);
    }

    @Test
    @DisplayName("Confirming a reservation increases cash balance by the total price")
    void confirmingReservationIncreasesCashBalance() throws Exception {
        double initialCash = getCashBalance();
        app.processLine("RESERVE;alice;PEN-BLACK;10;10");
        app.processLine("CONFIRM;R2001");
        double finalCash = getCashBalance();
        assertEquals(initialCash + 15.0, finalCash, 0.001);
    }

    @Test
    @DisplayName("Confirming a non-existent reservation logs an error")
    void confirmingNonExistentReservationLogsError() throws Exception {
        app.processLine("CONFIRM;R9999");
        assertNull(getOrderStatus("O1001"));
    }

    @Test
    @DisplayName("Releasing a reservation returns stock to availability")
    void releasingReservationReturnsStockToAvailability() throws Exception {
        app.processLine("RESERVE;alice;PEN-BLACK;5;10");
        int reservedAfterReserve = getReservedBySku("PEN-BLACK");
        app.processLine("RELEASE;R2001");
        int reservedAfterRelease = getReservedBySku("PEN-BLACK");
        assertEquals(reservedAfterReserve - 5, reservedAfterRelease);
    }

    @Test
    @DisplayName("Releasing a reservation decreases reserved stock by the quantity")
    void releasingReservationDecreasesReservedStock() throws Exception {
        app.processLine("RESERVE;alice;PEN-BLACK;5;10");
        int reservedBefore = getReservedBySku("PEN-BLACK");
        app.processLine("RELEASE;R2001");
        int reservedAfter = getReservedBySku("PEN-BLACK");
        assertEquals(reservedBefore - 5, reservedAfter);
    }

    @Test
    @DisplayName("Releasing a reservation does not change on-hand stock")
    void releasingReservationDoesNotChangeOnHandStock() throws Exception {
        int initialStock = getStockBySku("PEN-BLACK");
        app.processLine("RESERVE;alice;PEN-BLACK;5;10");
        app.processLine("RELEASE;R2001");
        int finalStock = getStockBySku("PEN-BLACK");
        assertEquals(initialStock, finalStock);
    }

    @Test
    @DisplayName("Releasing a non-existent reservation logs an error")
    void releasingNonExistentReservationLogsError() throws Exception {
        int initialReserved = getReservedBySku("PEN-BLACK");
        app.processLine("RELEASE;R9999");
        int finalReserved = getReservedBySku("PEN-BLACK");
        assertEquals(initialReserved, finalReserved);
    }

    @Test
    @DisplayName("Reservations expire automatically after configured minutes")
    void reservationsExpireAutomatically() throws Exception {
        app.processLine("RESERVE;alice;PEN-BLACK;5;0");
        Thread.sleep(100);
        app.processLine("COUNT;PEN-BLACK");
        int reserved = getReservedBySku("PEN-BLACK");
        assertEquals(0, reserved);
    }

    @Test
    @DisplayName("Expired reservations return stock to availability")
    void expiredReservationsReturnStockToAvailability() throws Exception {
        app.processLine("RESERVE;alice;PEN-BLACK;5;0");
        int reservedBefore = getReservedBySku("PEN-BLACK");
        assertEquals(5, reservedBefore);
        Thread.sleep(100);
        app.processLine("COUNT;PEN-BLACK");
        int reservedAfter = getReservedBySku("PEN-BLACK");
        assertEquals(0, reservedAfter);
    }

    @Test
    @DisplayName("Expired reservations decrease reserved stock by the quantity")
    void expiredReservationsDecreaseReservedStock() throws Exception {
        app.processLine("RESERVE;alice;PEN-BLACK;5;0");
        Thread.sleep(100);
        app.processLine("COUNT;PEN-BLACK");
        int reserved = getReservedBySku("PEN-BLACK");
        assertEquals(0, reserved);
    }

    @Test
    @DisplayName("Expired reservations do not change on-hand stock")
    void expiredReservationsDoNotChangeOnHandStock() throws Exception {
        int initialStock = getStockBySku("PEN-BLACK");
        app.processLine("RESERVE;alice;PEN-BLACK;5;0");
        Thread.sleep(100);
        app.processLine("COUNT;PEN-BLACK");
        int finalStock = getStockBySku("PEN-BLACK");
        assertEquals(initialStock, finalStock);
    }

    @Test
    @DisplayName("Confirming an expired reservation logs an error")
    void confirmingExpiredReservationLogsError() throws Exception {
        app.processLine("RESERVE;alice;PEN-BLACK;5;0");
        Thread.sleep(100);
        app.processLine("CONFIRM;R2001");
        assertNull(getOrderStatus("O1001"));
    }

    @Test
    @DisplayName("Multiple reservations can be created with unique IDs")
    void multipleReservationsHaveUniqueIds() throws Exception {
        app.processLine("RESERVE;alice;PEN-BLACK;5;10");
        app.processLine("RESERVE;bob;PEN-BLUE;3;10");
        Reservation res1 = getReservation("R2001");
        Reservation res2 = getReservation("R2002");
        assertNotNull(res1);
        assertNotNull(res2);
        assertNotEquals(res1.getReservationId(), res2.getReservationId());
    }

    @Test
    @DisplayName("Reservation reduces available stock for subsequent orders")
    void reservationReducesAvailableStock() throws Exception {
        app.processLine("RESERVE;alice;STAPLER;3;10");
        app.processLine("SELL;bob;STAPLER;2");
        String status = getOrderStatus("O1001");
        assertEquals("BACKORDER", status);
    }

    @Test
    @DisplayName("Confirmed reservation creates order with correct SKU and quantity")
    void confirmedReservationCreatesCorrectOrder() throws Exception {
        app.processLine("RESERVE;alice;PEN-BLACK;5;10");
        app.processLine("CONFIRM;R2001");
        Reservation reservation = getReservation("R2001");
        assertFalse(reservation.isActive());
    }

    @Test
    @DisplayName("Released reservation becomes inactive")
    void releasedReservationBecomesInactive() throws Exception {
        app.processLine("RESERVE;alice;PEN-BLACK;5;10");
        app.processLine("RELEASE;R2001");
        Reservation reservation = getReservation("R2001");
        assertFalse(reservation.isActive());
    }

    @Test
    @DisplayName("Cannot confirm already released reservation")
    void cannotConfirmReleasedReservation() throws Exception {
        app.processLine("RESERVE;alice;PEN-BLACK;5;10");
        app.processLine("RELEASE;R2001");
        app.processLine("CONFIRM;R2001");
        assertNull(getOrderStatus("O1001"));
    }

    @Test
    @DisplayName("Cannot release already confirmed reservation")
    void cannotReleaseConfirmedReservation() throws Exception {
        int initialReserved = getReservedBySku("PEN-BLACK");
        app.processLine("RESERVE;alice;PEN-BLACK;5;10");
        app.processLine("CONFIRM;R2001");
        app.processLine("RELEASE;R2001");
        int finalReserved = getReservedBySku("PEN-BLACK");
        assertEquals(initialReserved, finalReserved);
    }
}
