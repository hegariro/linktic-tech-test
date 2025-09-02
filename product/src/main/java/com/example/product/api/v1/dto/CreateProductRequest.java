package com.example.product.api.v1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank(message = "Product name cannot be blank") String name,
        String description,
        @NotNull(message = "Product price cannot be null") BigDecimal price
) { }
