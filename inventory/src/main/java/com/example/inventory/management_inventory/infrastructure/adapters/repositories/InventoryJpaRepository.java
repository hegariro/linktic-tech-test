package com.example.inventory.management_inventory.infrastructure.adapters.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inventory.management_inventory.infrastructure.persistence.InventoryEntity;

public interface InventoryJpaRepository extends JpaRepository<InventoryEntity, String> {
    Optional<InventoryEntity> findByProductId(String productId);
}
