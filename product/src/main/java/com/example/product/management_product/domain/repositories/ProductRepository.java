package com.example.product.management_product.domain.repositories;

import com.example.product.management_product.domain.models.Product;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(UUID id);
}
