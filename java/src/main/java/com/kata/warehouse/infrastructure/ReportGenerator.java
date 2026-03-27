package com.kata.warehouse.infrastructure;

import com.kata.warehouse.WarehouseContext;
import com.kata.warehouse.domain.entity.Order;
import com.kata.warehouse.domain.repository.OrderRepository;
import com.kata.warehouse.domain.repository.ProductRepository;
import com.kata.warehouse.domain.valueobject.Quantity;
import com.kata.warehouse.domain.valueobject.SKU;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportGenerator {
    public void printEndOfDayReport(WarehouseContext context) {
        int shipped = 0;
        int backorder = 0;
        int cancelled = 0;

        try {
            Field orderRepoField = WarehouseContext.class.getDeclaredField("orderRepository");
            orderRepoField.setAccessible(true);
            OrderRepository orderRepository = (OrderRepository) orderRepoField.get(context);

            Field ordersField = OrderRepository.class.getDeclaredField("orders");
            ordersField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<?, Order> orders = (Map<?, Order>) ordersField.get(orderRepository);

            for (Order order : orders.values()) {
                if (order.getStatus() == Order.OrderStatus.SHIPPED) {
                    shipped++;
                } else if (order.getStatus() == Order.OrderStatus.BACKORDER) {
                    backorder++;
                } else if (order.isCancelled()) {
                    cancelled++;
                }
            }
        } catch (Exception e) {
            System.err.println("Error accessing order data: " + e.getMessage());
        }

        List<String> lowStock = new ArrayList<>();
        try {
            Field productRepoField = WarehouseContext.class.getDeclaredField("productRepository");
            productRepoField.setAccessible(true);
            ProductRepository productRepository = (ProductRepository) productRepoField.get(context);

            Field productsField = ProductRepository.class.getDeclaredField("products");
            productsField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<SKU, ?> products = (Map<SKU, ?>) productsField.get(productRepository);

            for (SKU sku : products.keySet()) {
                Quantity stock = context.getInventoryService().getStockOnHand(sku);
                if (stock.getValue() < 5) {
                    lowStock.add(sku.toString());
                }
            }
        } catch (Exception e) {
            System.err.println("Error accessing product data: " + e.getMessage());
        }

        System.out.println();
        System.out.println("==== end of day ====");
        System.out.println("orders shipped: " + shipped);
        System.out.println("orders backordered: " + backorder);
        System.out.println("orders cancelled: " + cancelled);
        System.out.println("cash balance: " + String.format("%.2f", context.getCashService().getCashBalance().getAmount()));
        System.out.println("low stock skus: " + lowStock);
        System.out.println();
        System.out.println("events:");
        for (String event : context.getEventLogService().getEventLog()) {
            System.out.println(" - " + event);
        }
    }
}
