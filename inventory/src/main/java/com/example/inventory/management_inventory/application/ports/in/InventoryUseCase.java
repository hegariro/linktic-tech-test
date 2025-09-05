package com.example.inventory.management_inventory.application.ports.in;

import java.util.List;
import java.util.Optional;

import com.example.inventory.management_inventory.domain.models.Inventory;
import com.example.inventory.management_inventory.domain.models.TransactionData;
import com.example.inventory.management_inventory.domain.models.TransactionType;

public interface InventoryUseCase {
  Optional<Inventory> findByIdProduct(String idProduct);
  Optional<TransactionData> modifyInventory(TransactionType transactionType, List<Inventory> inventoryList);
}
