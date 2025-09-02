package com.example.product.management_product.application.ports;

import java.math.BigDecimal;

public interface CreateProductCommand {
    void createProduct(String name, String description, BigDecimal price);
}
