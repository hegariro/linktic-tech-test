package com.example.inventory.management_inventory.infrastructure.adapters.repositories.mapper;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.inventory.management_inventory.domain.models.Inventory;
import com.example.inventory.management_inventory.infrastructure.persistence.InventoryEntity;

@Component
public class InventoryPersistenceMapper {
    
    public Optional<Inventory> toDomain(InventoryEntity entity) {
            Inventory inventory = new Inventory(entity.getId(), entity.getProductId(), entity.getQuantityAvailable());

            return Optional.of(inventory);
    }
}
