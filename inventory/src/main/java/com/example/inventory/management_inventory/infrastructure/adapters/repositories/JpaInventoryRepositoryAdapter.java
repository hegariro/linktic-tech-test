package com.example.inventory.management_inventory.infrastructure.adapters.repositories;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.inventory.management_inventory.domain.models.Inventory;
import com.example.inventory.management_inventory.domain.repositories.InventoryRepository;
import com.example.inventory.management_inventory.infrastructure.adapters.repositories.mapper.InventoryPersistenceMapper;

@Component
public class JpaInventoryRepositoryAdapter implements InventoryRepository {

    private final InventoryJpaRepository jpaRepository;
    private final InventoryPersistenceMapper mapper;

    public JpaInventoryRepositoryAdapter(
        InventoryJpaRepository jpaRepository,
        InventoryPersistenceMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Inventory> findByIdProduct(String idProduct) {
        var response = jpaRepository.findByProductId(idProduct);
        return response.map(mapper::toDomain).orElse(Optional.empty());
    }
    
}
