package com.example.inventory.api.v1.dto;

import java.math.BigDecimal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SellItemAttribs(
    @NotEmpty @Valid String productId,
    @NotNull @Positive int quantity,
    @NotNull @Positive BigDecimal subtotal
) { }
