package com.example.inventory.management_product.application.ports.in;

import com.example.inventory.management_product.domain.models.Product;

import java.util.Optional;

public interface ProductUseCase {
    Optional<Product> findById(String id);
}
