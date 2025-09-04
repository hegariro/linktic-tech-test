package com.example.inventory.management_inventory.infrastructure;

import java.util.Optional;

import org.springframework.web.bind.annotation.*;

import com.example.inventory.api.v1.dto.InventoryDomainResponse;
import com.example.inventory.management_inventory.infrastructure.mapper.InventoryMapper;
import com.example.inventory.management_inventory.application.ports.in.InventoryUseCase;

@RestController
public class InventoryController {
    
    private final InventoryUseCase inventoryUseCase;
    private final InventoryMapper mapper;

    public InventoryController(InventoryUseCase inventoryUseCase, InventoryMapper mapper) {
        this.inventoryUseCase = inventoryUseCase;
        this.mapper = mapper;
    }

    public Optional<InventoryDomainResponse> findByIdProduct(String idProduct) {
        var inventoryResponse = inventoryUseCase.findByIdProduct(idProduct);
        return mapper.toInventoryResponse(inventoryResponse);
    }
}
