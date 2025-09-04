package com.example.inventory.management_inventory.domain.repositories;

import java.util.Optional;

import com.example.inventory.management_inventory.domain.models.Inventory;

public interface InventoryRepository {
  Optional<Inventory> findByIdProduct(String idProduct);
}
