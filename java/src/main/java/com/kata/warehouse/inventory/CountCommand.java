package com.kata.warehouse.inventory;

import com.kata.warehouse.WarehouseContext;
import com.kata.warehouse.shared.Quantity;
import com.kata.warehouse.shared.SKU;

public class CountCommand implements com.kata.warehouse.Command {
    @Override
    public void execute(WarehouseContext context, String[] parts) {
        SKU sku = new SKU(parts[1]);
        Quantity onHand = context.getInventoryService().getStockOnHand(sku);
        Quantity reserved = context.getInventoryService().getReservedQuantity(sku);
        Quantity available = onHand.subtract(reserved);
        context.getEventLogService().addEvent(sku + " onhand=" + onHand + " reserved=" + reserved + " available=" + available);
    }
}
