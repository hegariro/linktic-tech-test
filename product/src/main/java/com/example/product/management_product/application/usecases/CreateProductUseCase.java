package com.example.product.management_product.application.usecases;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.product.management_product.application.ports.ProductCommand;
import com.example.product.management_product.domain.models.Product;
import com.example.product.management_product.domain.repositories.ProductRepository;

@Component
public class CreateProductUseCase implements ProductCommand {

    private final ProductRepository productRepository;

    public CreateProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(String name, String description, BigDecimal price) {
        Product product = Product.create(name, description, price);
        productRepository.save(product);
        return product;
    }

    @Override
    public Product getProductByID(String id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product "+id+" not fond"));
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll()
            .orElseThrow(() -> new RuntimeException("Empty database"));
    }
}
