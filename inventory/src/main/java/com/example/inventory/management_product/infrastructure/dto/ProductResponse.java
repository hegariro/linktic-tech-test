package com.example.inventory.management_product.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ProductResponse(
    @JsonProperty("data") Data data
) {
    public record Data(
        String id,
        String type,
        Attributes attributes
    ) {}

    public record Attributes(
        String name,
        String description,
        BigDecimal price
    ) {}
}
