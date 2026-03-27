package com.kata.warehouse.reservation;

import com.kata.warehouse.Command;
import com.kata.warehouse.WarehouseContext;
import com.kata.warehouse.shared.Quantity;
import com.kata.warehouse.shared.SKU;

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
