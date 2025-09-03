package com.example.inventory.management_product.application.usecases;

import java.util.Optional;

import com.example.inventory.management_product.application.ports.in.ProductUseCase;
import com.example.inventory.management_product.domain.models.Product;
import com.example.inventory.management_product.application.ports.out.ProductRepository;

public class ProductUseCaseImpl implements ProductUseCase {

    private final ProductRepository productRepository;

    public ProductUseCaseImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }
}
