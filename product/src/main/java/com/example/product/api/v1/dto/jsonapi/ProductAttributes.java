package com.example.product.api.v1.dto.jsonapi;

import java.math.BigDecimal;
public record ProductAttributes(
        String name,
        String description,
        BigDecimal price
) {}