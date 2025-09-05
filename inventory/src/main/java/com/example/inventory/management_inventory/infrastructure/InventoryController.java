package com.example.inventory.management_inventory.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RestController;

import com.example.inventory.api.v1.dto.BuyProductsJsonApiAttribs;
import com.example.inventory.api.v1.dto.BuyProductsResponse;
import com.example.inventory.api.v1.dto.InventoryDomainResponse;
import com.example.inventory.management_inventory.application.ports.in.InventoryUseCase;
import com.example.inventory.management_inventory.domain.models.Inventory;
import com.example.inventory.management_inventory.domain.models.TransactionData;
import com.example.inventory.management_inventory.domain.models.TransactionType;
import com.example.inventory.management_inventory.infrastructure.mapper.InventoryMapper;

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
        return mapper.toApi(inventoryResponse);
    }

    public Optional<BuyProductsResponse> buyProducts(BuyProductsJsonApiAttribs products) {
        List<Inventory> inventoryList = mapper.toDomain(products);
        var buyResponse = inventoryUseCase.modifyInventory(TransactionType.INBOUND, inventoryList);

        if (!buyResponse.isPresent()) {
            return Optional.empty();
        }

        TransactionData response = buyResponse.get();

        return mapper.toApi(response);
    }
}
