package com.example.product.management_product.domain.models;

import java.math.BigDecimal;
import java.util.UUID;

public record Product(
        UUID id,
        String name,
        String description,
        BigDecimal price
) {
    // Constructor de fábrica para crear un nuevo producto con un ID generado
    public static Product create(String name, String description, BigDecimal price) {
        return new Product(UUID.randomUUID(), name, description, price);
    }
}
