package com.example.inventory.management_product.application.ports.out;

import com.example.inventory.management_product.domain.models.Product;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(String id);
}
