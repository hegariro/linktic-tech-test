package com.example.inventory.management_inventory.infrastructure.mapper;

import java.util.Optional;

import com.example.inventory.api.v1.dto.InventoryDomainResponse;
import com.example.inventory.management_inventory.domain.models.Inventory;

public interface InventoryMapper {
    Optional<InventoryDomainResponse> toInventoryResponse(Optional<Inventory> inventory);
}
