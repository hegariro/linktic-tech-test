package com.example.product.management_product.application.usecases;

import com.example.product.management_product.application.ports.CreateProductCommand;
import com.example.product.management_product.domain.repositories.ProductRepository;
import com.example.product.management_product.domain.models.Product;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class CreateProductUseCase implements CreateProductCommand {

    private final ProductRepository productRepository;

    public CreateProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(String name, String description, BigDecimal price) {
        // Aquí se crea la entidad de dominio y se utiliza el repositorio.
        Product product = Product.create(name, description, price);
        productRepository.save(product);
        return product;
    }
}
