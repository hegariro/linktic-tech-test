package com.example.product.management_product.application.ports;

import java.math.BigDecimal;
import java.util.List;

import com.example.product.management_product.domain.models.Product;

public interface ProductCommand {
    Product createProduct(String name, String description, BigDecimal price);
    Product getProductByID(String id);
    List<Product> getProducts();
}
