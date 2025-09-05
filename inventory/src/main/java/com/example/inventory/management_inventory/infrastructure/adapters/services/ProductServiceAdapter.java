package com.example.inventory.management_inventory.infrastructure.adapters.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.inventory.api.v1.dto.ProductResponse;
import com.example.inventory.management_inventory.domain.models.Product;
import com.example.inventory.management_inventory.domain.repositories.ProductServiceRespository;
import com.example.inventory.management_inventory.infrastructure.mapper.InventoryMapper;
import com.example.inventory.management_product.infrastructure.ProductController;

@Service("productServiceManagementInventoryAdapter")
public class ProductServiceAdapter implements ProductServiceRespository {
    
    private final ProductController productController;
    private final InventoryMapper mapper;

    public ProductServiceAdapter(
        ProductController productController,
        InventoryMapper mapper
    ) {
        this.productController = productController;
        this.mapper = mapper;
    }

    @Override
    public Optional<Product> searchProductById(String id) {
        try {
            Optional<ProductResponse> productResponse = productController.findByID(id);

            return mapper.toDomain(productResponse);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
}
