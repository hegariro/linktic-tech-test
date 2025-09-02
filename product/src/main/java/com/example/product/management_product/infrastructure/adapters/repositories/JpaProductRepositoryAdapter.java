package com.example.product.management_product.infrastructure.adapters.repositories;

import com.example.product.management_product.domain.models.Product;
import com.example.product.management_product.domain.repositories.ProductRepository;
import com.example.product.management_product.infrastructure.persistence.ProductEntity;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
    public Optional<Product> findById(UUID id) {
        Optional<ProductEntity> entity = jpaRepository.findById(id);
        return entity.map(e -> new Product(e.getId(), e.getName(), e.getDescription(), e.getPrice()));
    }
}
