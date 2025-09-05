package com.example.inventory.management_inventory.domain.repositories;

import java.util.Optional;

import com.example.inventory.management_inventory.domain.models.Product;

public interface ProductServiceRespository {
  Optional<Product> searchProductById(String id);
}
