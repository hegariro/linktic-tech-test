package com.example.inventory.management_inventory.application.ports.out;

import java.util.Optional;

import com.example.inventory.management_inventory.domain.models.Product;

public interface InventoryRepository {
    Optional<Product> findProductById(String id);
}
