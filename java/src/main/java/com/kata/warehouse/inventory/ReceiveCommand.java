package com.kata.warehouse.inventory;

import com.kata.warehouse.WarehouseContext;
import com.kata.warehouse.shared.Money;
import com.kata.warehouse.shared.Quantity;
import com.kata.warehouse.shared.SKU;

public class ReceiveCommand implements com.kata.warehouse.Command {
    @Override
    public void execute(WarehouseContext context, String[] parts) {
        SKU sku = new SKU(parts[1]);
        Quantity qty = new Quantity(context.parseInt(parts[2]));
        Money unitCost = new Money(context.parseDouble(parts[3]));
        
        context.getInventoryService().receiveStock(sku, qty, unitCost);
        context.getCashService().deductCash(unitCost.multiply(qty.getValue()));
        context.getEventLogService().addEvent("received " + qty + " of " + sku + " at " + unitCost);
    }
}
