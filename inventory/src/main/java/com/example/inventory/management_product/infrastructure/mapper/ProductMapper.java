package com.example.inventory.management_product.infrastructure.mapper;

import java.util.Optional;

import com.example.inventory.api.v1.dto.*;
import com.example.inventory.management_product.domain.models.Product;

public interface ProductMapper {
    Optional<ProductResponse> toProductResponse(Optional<Product> product);
}
