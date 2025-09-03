package com.example.inventory.management_product.infrastructure.mapper;

import com.example.inventory.management_product.domain.models.Product;
import com.example.inventory.management_product.infrastructure.dto.ProductResponse;

public class ProductServiceMapper {

    private ProductServiceMapper() {}

    public static Product toDomain(ProductResponse dto) {
        if (dto == null || dto.data() == null) {
            return null;
        }

        var data = dto.data();
        var attrs = data.attributes();

        return new Product(
            data.id(),
            attrs.name(),
            attrs.description(),
            attrs.price()
        );
    }
}
