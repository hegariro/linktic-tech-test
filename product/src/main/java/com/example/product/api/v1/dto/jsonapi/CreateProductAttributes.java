package com.example.product.api.v1.dto.jsonapi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateProductAttributes(
        @NotBlank String name,
        String description,
        @NotNull @Positive BigDecimal price
) {}