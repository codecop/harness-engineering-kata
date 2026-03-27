package com.kata.warehouse.inventory;

import com.kata.warehouse.shared.SKU;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ProductRepository {
    private final Map<SKU, Product> products = new HashMap<>();

    public Optional<Product> findBySku(SKU sku) {
        return Optional.ofNullable(products.get(sku));
    }

    public Product getOrCreate(SKU sku) {
        return products.computeIfAbsent(sku, Product::new);
    }

    public void save(Product product) {
        products.put(product.getSku(), product);
    }
}
