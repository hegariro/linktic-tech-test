package com.example.inventory.api.v1.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

public record ProductAttributes(
    @NotBlank String name,
    @NotBlank String description,
    @NotNull @Positive BigDecimal price
) { }
