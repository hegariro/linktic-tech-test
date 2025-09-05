package com.example.inventory.management_inventory.infrastructure.adapters.repositories;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.inventory.management_inventory.domain.models.Inventory;
import com.example.inventory.management_inventory.domain.models.TransactionData;
import com.example.inventory.management_inventory.domain.models.TransactionType;
import com.example.inventory.management_inventory.domain.repositories.InventoryRepository;
import com.example.inventory.management_inventory.infrastructure.adapters.repositories.mapper.InventoryPersistenceMapper;
import com.example.inventory.management_inventory.infrastructure.persistence.InventoryEntity;

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

    @Override
    public Optional<TransactionData> modifyInventory(TransactionType transactionType, Inventory inventory) {
        Optional<TransactionData> response = Optional.empty();
        switch (transactionType){
            case INBOUND -> response = inbounUpdateInventory(inventory);
            case OUTBOUND -> response = outbounUpdateInventory(inventory);
        }

        return response;
    }
    
    private Optional<TransactionData> inbounUpdateInventory(Inventory inventory) {
        try {
            InventoryEntity inventoryData = jpaRepository.findByProductId(inventory.idProduct()).get();
            
            int quantityAvailable = inventoryData.getQuantityAvailable() + inventory.quantityAvailable();
            return exucuteTransaction(new InventoryEntity(
                inventoryData.getId(),
                inventoryData.getProductId(),
                quantityAvailable
            ));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("----------------------------------------  inbounUpdateInventory");
        }
        return Optional.empty();
    }

    private Optional<TransactionData> outbounUpdateInventory(Inventory inventory) {
        try {
            InventoryEntity inventoryData = jpaRepository.findByProductId(inventory.idProduct()).get();
        
            if (inventoryData.getQuantityAvailable() < inventory.quantityAvailable()) {
                System.err.println("Overflow quantity required. Max quantity: " + inventoryData.getQuantityAvailable());
                System.err.println("----------------------------------------  outbounUpdateInventory");
                return Optional.empty();
            }
        
            int quantityAvailable = inventoryData.getQuantityAvailable() - inventory.quantityAvailable();
            return exucuteTransaction(new InventoryEntity(
                inventoryData.getId(),
                inventoryData.getProductId(),
                quantityAvailable
            ));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("----------------------------------------  inbounUpdateInventory");
        }
        return Optional.empty();
    }

    private Optional<TransactionData> exucuteTransaction(InventoryEntity entity) {
        try {
            InventoryEntity updated = jpaRepository.save(entity);
            List<String> productIdList = new ArrayList<>();
            productIdList.add(updated.getProductId());

            return Optional.of(new TransactionData(
                UUID.randomUUID().toString(),
                TransactionType.INBOUND, 
                updated.getQuantityAvailable(),
                0,
                BigDecimal.ZERO,
                productIdList
            ));
        } catch (Exception e) {
            System.err.println(e.getClass());
            System.err.println(e.getMessage());
            return Optional.empty();
        }
    }
}

