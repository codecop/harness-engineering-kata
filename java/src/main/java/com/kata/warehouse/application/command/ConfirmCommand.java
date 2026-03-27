package com.kata.warehouse.application.command;

import com.kata.warehouse.WarehouseContext;
import com.kata.warehouse.domain.entity.Order;
import com.kata.warehouse.domain.entity.Reservation;
import com.kata.warehouse.domain.valueobject.Money;
import com.kata.warehouse.domain.valueobject.OrderId;
import com.kata.warehouse.domain.valueobject.ReservationId;

public class ConfirmCommand implements Command {
    @Override
    public void execute(WarehouseContext context, String[] parts) {
        ReservationId reservationId = new ReservationId(parts[1]);
        Reservation reservation = context.getReservationService().getReservation(reservationId);

        if (reservation == null) {
            context.getEventLogService().addEvent("cannot confirm " + reservationId + " because it does not exist");
            return;
        }

        if (!reservation.isActive()) {
            context.getEventLogService().addEvent("cannot confirm " + reservationId + " because it is no longer active");
            return;
        }

        if (reservation.isExpired()) {
            context.getEventLogService().addEvent("cannot confirm " + reservationId + " because it has expired");
            return;
        }

        OrderId orderId = context.getOrderService().generateOrderId();
        context.getInventoryService().removeStock(reservation.getSku(), reservation.getQuantity());
        context.getInventoryService().releaseReservedStock(reservation.getSku(), reservation.getQuantity());
        
        Money orderTotal = context.getOrderService().calculateOrderTotal(reservation.getSku(), reservation.getQuantity());
        context.getCashService().addCash(orderTotal);
        
        context.getOrderService().createOrder(orderId, reservation.getSku(), reservation.getQuantity(), Order.OrderStatus.SHIPPED);
        reservation.deactivate();
        
        context.getEventLogService().addEvent("reservation " + reservationId + " confirmed as order " + orderId + " amount=" + orderTotal);
    }
}
