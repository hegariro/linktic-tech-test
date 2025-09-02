package com.example.product.api.v1.dto.jsonapi;

import java.util.List;

import com.example.product.management_product.domain.models.Product;

public record ProductResponse(
        String id,
        String type,
        ProductAttributes attributes
) {
    public static ProductResponse fromDomain(Product product) {
        return new ProductResponse(
                product.id(),
                "products",
                new ProductAttributes(product.name(), product.description(), product.price())
        );
    }

    public static List<ProductResponse> fromDomain (List<Product> list) {
        return list.stream().map(e -> new ProductResponse(
            e.id(), 
            "products", 
            new ProductAttributes(
                e.name(), e.description(), e.price()
            )
        )).toList();
    }
}