package com.example.inventory.management_product.domain.models;

import java.math.BigDecimal;

public record Product(
    String id, String name, String description, BigDecimal price
) { }
