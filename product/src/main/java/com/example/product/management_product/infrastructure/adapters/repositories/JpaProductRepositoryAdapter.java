package com.example.product.management_product.infrastructure.adapters.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.product.management_product.domain.models.Product;
import com.example.product.management_product.domain.repositories.ProductRepository;
import com.example.product.management_product.infrastructure.persistence.ProductEntity;

@Component
public class JpaProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository jpaRepository;

    public JpaProductRepositoryAdapter(ProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Product save(Product product) {
        ProductEntity productEntity = new ProductEntity(
                product.id(),
                product.name(),
                product.description(),
                product.price()
        );
        // Llama al metodo save() de Spring Data JPA
        ProductEntity savedEntity = jpaRepository.save(productEntity);
        return new Product(
                savedEntity.getId(),
                savedEntity.getName(),
                savedEntity.getDescription(),
                savedEntity.getPrice()
        );
    }

    @Override
    public Optional<Product> findById(String id) {
        Optional<ProductEntity> entity = jpaRepository.findById(id);
        return entity.map(e -> new Product(e.getId(), e.getName(), e.getDescription(), e.getPrice()));
    }

    @Override
    public Optional<List<Product>> findAll() {
        List<ProductEntity> entities = jpaRepository.findAll();
        if (entities.isEmpty()) {
            return Optional.empty(); 
        }

        List<Product> products = entities.stream()
            .map(e -> new Product(e.getId(), e.getName(), e.getDescription(), e.getPrice()))
            .toList();
        return Optional.of(products);
    }
}
