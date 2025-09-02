package com.example.product.api.v1.dto.jsonapi;

import com.example.product.management_product.domain.models.Product;

public record ProductResponse(
        String id,
        String type,
        ProductAttributes attributes
) {
    public static ProductResponse fromDomain(Product product) {
        return new ProductResponse(
                product.id().toString(),
                "products",
                new ProductAttributes(product.name(), product.description(), product.price())
        );
    }
}