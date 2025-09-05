package com.example.inventory.management_inventory.infrastructure.mapper;

import java.util.List;
import java.util.Optional;

import com.example.inventory.api.v1.dto.BuyProductsJsonApiAttribs;
import com.example.inventory.api.v1.dto.BuyProductsResponse;
import com.example.inventory.api.v1.dto.InventoryDomainResponse;
import com.example.inventory.api.v1.dto.ProductResponse;
import com.example.inventory.api.v1.dto.SellProductsJsonApiAttribs;
import com.example.inventory.api.v1.dto.SellProductsResponse;
import com.example.inventory.management_inventory.domain.models.Inventory;
import com.example.inventory.management_inventory.domain.models.Product;
import com.example.inventory.management_inventory.domain.models.TransactionData;

public interface InventoryMapper {
    Optional<InventoryDomainResponse> toApi(Optional<Inventory> inventory);
    Optional<BuyProductsResponse> toApi(TransactionData data);
    Optional<SellProductsResponse> toApiSell(TransactionData data);
    List<Inventory> toDomain(BuyProductsJsonApiAttribs inboudProducts);
    List<Inventory> toDomain(SellProductsJsonApiAttribs inboudProducts);
    Optional<Product> toDomain(Optional<ProductResponse> product);
}
