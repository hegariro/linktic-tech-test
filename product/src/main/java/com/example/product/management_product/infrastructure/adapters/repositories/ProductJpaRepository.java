package com.example.product.management_product.infrastructure.adapters.repositories;

import com.example.product.management_product.infrastructure.persistence.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {
    // Spring Data JPA se encarga de la implementacion,
    // proveyendo metodos como save(), findById(), etc.
}
