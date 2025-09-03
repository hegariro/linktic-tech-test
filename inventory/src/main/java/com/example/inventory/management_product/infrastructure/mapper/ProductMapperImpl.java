package com.example.inventory.management_product.infrastructure.mapper;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.inventory.api.v1.dto.ProductResponse;
import com.example.inventory.management_product.domain.models.Product;

@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Optional<ProductResponse> toProductResponse(Optional<Product> productOpt) {
        return productOpt.map(product -> new ProductResponse(
            product.id(), product.name(), product.description(), product.price())
        );
    }
}

