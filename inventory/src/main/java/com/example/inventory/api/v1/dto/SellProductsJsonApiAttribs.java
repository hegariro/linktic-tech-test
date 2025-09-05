package com.example.inventory.api.v1.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SellProductsJsonApiAttribs(
    @NotNull @Positive BigDecimal total,
    List<SellItemAttribs> items
) { }
