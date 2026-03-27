package com.kata.warehouse.domain.service;

import com.kata.warehouse.domain.entity.Product;
import com.kata.warehouse.domain.repository.ProductRepository;
import com.kata.warehouse.domain.valueobject.Money;
import com.kata.warehouse.domain.valueobject.Quantity;
import com.kata.warehouse.domain.valueobject.SKU;

public class InventoryService {
    private final ProductRepository productRepository;

    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void receiveStock(SKU sku, Quantity quantity, Money unitCost) {
        Product product = productRepository.getOrCreate(sku);
        product.addStock(quantity);
        product.setUnitPrice(unitCost);
        productRepository.save(product);
    }

    public boolean hasAvailableStock(SKU sku, Quantity required) {
        return productRepository.findBySku(sku)
                .map(product -> product.hasAvailableStock(required))
                .orElse(false);
    }

    public void reserveStock(SKU sku, Quantity quantity) {
        Product product = productRepository.getOrCreate(sku);
        product.addReservation(quantity);
        productRepository.save(product);
    }

    public void releaseReservedStock(SKU sku, Quantity quantity) {
        Product product = productRepository.getOrCreate(sku);
        product.removeReservation(quantity);
        productRepository.save(product);
    }

    public void removeStock(SKU sku, Quantity quantity) {
        Product product = productRepository.getOrCreate(sku);
        product.removeStock(quantity);
        productRepository.save(product);
    }

    public void addStock(SKU sku, Quantity quantity) {
        Product product = productRepository.getOrCreate(sku);
        product.addStock(quantity);
        productRepository.save(product);
    }

    public Money getUnitPrice(SKU sku) {
        return productRepository.findBySku(sku)
                .map(Product::getUnitPrice)
                .orElse(new Money(0.0));
    }

    public Quantity getStockOnHand(SKU sku) {
        return productRepository.findBySku(sku)
                .map(Product::getStockOnHand)
                .orElse(new Quantity(0));
    }

    public Quantity getReservedQuantity(SKU sku) {
        return productRepository.findBySku(sku)
                .map(Product::getReservedQuantity)
                .orElse(new Quantity(0));
    }
}
