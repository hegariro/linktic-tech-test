package com.example.product.management_product.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.example.product.management_product.domain.models.Product;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(String id);
    Optional<List<Product>> findAll();
}
