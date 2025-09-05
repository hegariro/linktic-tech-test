package com.example.inventory.management_inventory.domain.repositories;

import java.util.Optional;

import com.example.inventory.management_inventory.domain.models.Inventory;
import com.example.inventory.management_inventory.domain.models.TransactionData;
import com.example.inventory.management_inventory.domain.models.TransactionType;

public interface InventoryRepository {
  Optional<Inventory> findByIdProduct(String idProduct);
  Optional<TransactionData> modifyInventory(TransactionType transactionType, Inventory inventory);
}
