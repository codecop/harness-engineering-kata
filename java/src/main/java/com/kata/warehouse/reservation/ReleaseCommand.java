package com.kata.warehouse.reservation;

import com.kata.warehouse.Command;
import com.kata.warehouse.WarehouseContext;

public class ReleaseCommand implements Command {
    @Override
    public void execute(WarehouseContext context, String[] parts) {
        ReservationId reservationId = new ReservationId(parts[1]);
        Reservation reservation = context.getReservationService().getReservation(reservationId);

        if (reservation == null) {
            context.getEventLogService().addEvent("cannot release " + reservationId + " because it does not exist");
            return;
        }

        if (!reservation.isActive()) {
            context.getEventLogService().addEvent("cannot release " + reservationId + " because it is no longer active");
            return;
        }

        context.getReservationService().releaseReservation(reservationId);
        context.getEventLogService().addEvent("reservation " + reservationId + " released");
    }
}
