package com.kata.warehouse;

public class ReleaseCommand implements Command {
    private final String reservationId;

    public ReleaseCommand(String reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public void execute(WarehouseContext context) {
        Reservation reservation = context.getReservationManager().getReservation(reservationId);
        if (reservation == null) {
            context.getEventLog().add("cannot release " + reservationId + " because it does not exist");
            return;
        }
        
        if (!reservation.isActive()) {
            context.getEventLog().add("cannot release " + reservationId + " because it is no longer active");
            return;
        }
        
        String sku = reservation.getSku();
        int quantity = reservation.getQuantity();
        
        context.getStockManager().removeReservation(sku, quantity);
        reservation.deactivate();
        
        context.getEventLog().add("reservation " + reservationId + " released, returned " + quantity + " of " + sku + " to available stock");
    }
}
