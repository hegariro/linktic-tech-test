package com.example.product.management_product.infrastructure.adapters.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.product.management_product.infrastructure.persistence.ProductEntity;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, String> {
    // Spring Data JPA se encarga de la implementacion,
    // proveyendo metodos como save(), findById(), etc.
}
