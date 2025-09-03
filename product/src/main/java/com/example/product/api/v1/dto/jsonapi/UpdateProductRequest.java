package com.example.product.api.v1.dto.jsonapi;

import java.math.BigDecimal;

import jakarta.validation.constraints.Positive;

public record UpdateProductRequest(
    UpdateProductData data
) {
    public record UpdateProductData(
        String name,
        String description,
        @Positive BigDecimal price
    ) {}
}
