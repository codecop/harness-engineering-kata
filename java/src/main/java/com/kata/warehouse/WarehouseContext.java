package com.kata.warehouse;

import com.kata.warehouse.financial.CashService;
import com.kata.warehouse.inventory.InventoryService;
import com.kata.warehouse.inventory.ProductRepository;
import com.kata.warehouse.order.OrderRepository;
import com.kata.warehouse.order.OrderService;
import com.kata.warehouse.reservation.ReservationRepository;
import com.kata.warehouse.reservation.ReservationService;
import com.kata.warehouse.shared.EventLogService;

public class WarehouseContext {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReservationRepository reservationRepository;
    private final InventoryService inventoryService;
    private final OrderService orderService;
    private final ReservationService reservationService;
    private final CashService cashService;
    private final EventLogService eventLogService;

    public WarehouseContext() {
        this.productRepository = new ProductRepository();
        this.orderRepository = new OrderRepository();
        this.reservationRepository = new ReservationRepository();
        this.inventoryService = new InventoryService(productRepository);
        this.orderService = new OrderService(orderRepository, inventoryService);
        this.reservationService = new ReservationService(reservationRepository, inventoryService);
        this.cashService = new CashService();
        this.eventLogService = new EventLogService();
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public ReservationService getReservationService() {
        return reservationService;
    }

    public CashService getCashService() {
        return cashService;
    }

    public EventLogService getEventLogService() {
        return eventLogService;
    }

    public int parseInt(String value) {
        return Integer.parseInt(value.trim());
    }

    public double parseDouble(String value) {
        return Double.parseDouble(value.trim());
    }
}
