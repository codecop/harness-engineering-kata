package com.kata.warehouse.domain.entity;

import com.kata.warehouse.domain.valueobject.Money;
import com.kata.warehouse.domain.valueobject.Quantity;
import com.kata.warehouse.domain.valueobject.SKU;

public class Product {
    private final SKU sku;
    private Quantity stockOnHand;
    private Quantity reservedQuantity;
    private Money unitPrice;

    public Product(SKU sku) {
        this.sku = sku;
        this.stockOnHand = new Quantity(0);
        this.reservedQuantity = new Quantity(0);
        this.unitPrice = new Money(0.0);
    }

    public SKU getSku() {
        return sku;
    }

    public Quantity getStockOnHand() {
        return stockOnHand;
    }

    public Quantity getReservedQuantity() {
        return reservedQuantity;
    }

    public Quantity getAvailableQuantity() {
        return stockOnHand.subtract(reservedQuantity);
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Money unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void addStock(Quantity quantity) {
        this.stockOnHand = this.stockOnHand.add(quantity);
    }

    public void removeStock(Quantity quantity) {
        this.stockOnHand = this.stockOnHand.subtract(quantity);
    }

    public void addReservation(Quantity quantity) {
        this.reservedQuantity = this.reservedQuantity.add(quantity);
    }

    public void removeReservation(Quantity quantity) {
        this.reservedQuantity = this.reservedQuantity.subtract(quantity);
    }

    public boolean hasAvailableStock(Quantity required) {
        return getAvailableQuantity().isGreaterThanOrEqual(required);
    }
}
