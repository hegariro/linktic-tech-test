package com.example.product.management_product.application.ports;

import com.example.product.management_product.domain.models.Product;
import java.math.BigDecimal;

public interface CreateProductCommand {
    Product createProduct(String name, String description, BigDecimal price);
}
