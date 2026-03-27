package com.kata.warehouse.application.command;

import com.kata.warehouse.WarehouseContext;
import com.kata.warehouse.domain.entity.Reservation;
import com.kata.warehouse.domain.valueobject.Quantity;
import com.kata.warehouse.domain.valueobject.ReservationId;
import com.kata.warehouse.domain.valueobject.SKU;

public class ReserveCommand implements Command {
    @Override
    public void execute(WarehouseContext context, String[] parts) {
        String customer = parts[1];
        SKU sku = new SKU(parts[2]);
        Quantity qty = new Quantity(context.parseInt(parts[3]));
        int expiryMinutes = context.parseInt(parts[4]);
        
        ReservationId reservationId = context.getReservationService().generateReservationId();
        Reservation reservation = context.getReservationService().createReservation(reservationId, customer, sku, qty, expiryMinutes);
        
        if (reservation == null) {
            context.getEventLogService().addEvent("reservation " + reservationId + " failed for " + customer + " - insufficient stock");
        } else {
            context.getEventLogService().addEvent("reservation " + reservationId + " created for " + customer + " sku=" + sku + " qty=" + qty + " expires in " + expiryMinutes + " minutes");
        }
    }
}
