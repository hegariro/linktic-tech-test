package com.example.inventory.management_inventory.infrastructure.mapper;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.inventory.api.v1.dto.BuyProductsJsonApiAttribs;
import com.example.inventory.api.v1.dto.BuyProductsResponse;
import com.example.inventory.api.v1.dto.InventoryDomainResponse;
import com.example.inventory.api.v1.dto.ProductResponse;
import com.example.inventory.api.v1.dto.SellProductsJsonApiAttribs;
import com.example.inventory.api.v1.dto.SellProductsResponse;
import com.example.inventory.management_inventory.domain.models.Inventory;
import com.example.inventory.management_inventory.domain.models.Product;
import com.example.inventory.management_inventory.domain.models.TransactionData;

@Component
public class InventoryMapperImpl implements InventoryMapper {
    
    @Override
    public Optional<InventoryDomainResponse> toApi(Optional<Inventory> request) {
        return request.map(inventory -> new InventoryDomainResponse(
            inventory.id(), inventory.idProduct(), inventory.quantityAvailable()
        ));
    }
    
    @Override
    public List<Inventory> toDomain(BuyProductsJsonApiAttribs inboudProducts) {
        var products = inboudProducts.items();
        return products.stream().map(p -> new Inventory(
            p.productId(), p.quantity()
        )).toList();
    }
    
    @Override
    public Optional<BuyProductsResponse> toApi(TransactionData data) {
        return Optional.of(new BuyProductsResponse(data.id(), data.totalItemsOk(), data.total(), data.idItemsList()));
    }

    @Override
    public Optional<Product> toDomain(Optional<ProductResponse> productResponse) {
        try {
            if (!productResponse.isPresent()) {
                throw new Exception("Product not found");
            }

            ProductResponse product = productResponse.get();
            return Optional.of(new Product(
                product.id(), product.name(), product.description(), product.price()
            ));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<SellProductsResponse> toApiSell(TransactionData data) {
        return Optional.of(new SellProductsResponse(data.id(), data.totalItemsOk(), data.total(), data.idItemsList()));
    }

    @Override
    public List<Inventory> toDomain(SellProductsJsonApiAttribs inboudProducts) {
        var products = inboudProducts.items();
        return products.stream().map(p -> new Inventory(
            p.productId(), p.quantity()
        )).toList();
    }
}
