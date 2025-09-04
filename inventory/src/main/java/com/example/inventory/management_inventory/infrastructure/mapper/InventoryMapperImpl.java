package com.example.inventory.management_inventory.infrastructure.mapper;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.inventory.api.v1.dto.InventoryDomainResponse;
import com.example.inventory.management_inventory.domain.models.Inventory;

@Component
public class InventoryMapperImpl implements InventoryMapper {
    
    @Override
    public Optional<InventoryDomainResponse> toInventoryResponse(Optional<Inventory> request) {
        return request.map(inventory -> new InventoryDomainResponse(
            inventory.id(), inventory.idProduct(), inventory.quantityAvailable()
        ));
    }
}
