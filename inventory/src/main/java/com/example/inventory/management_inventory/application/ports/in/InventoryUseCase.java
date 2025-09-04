package com.example.inventory.management_inventory.application.ports.in;

import com.example.inventory.management_inventory.domain.models.Inventory;

import java.util.Optional;

public interface InventoryUseCase {
  Optional<Inventory> findByIdProduct(String idProduct);
}
