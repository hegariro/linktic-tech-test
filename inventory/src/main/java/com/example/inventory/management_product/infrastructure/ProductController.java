package com.example.inventory.management_product.infrastructure;

import java.util.Optional;

import com.example.inventory.api.v1.dto.ProductResponse;
import com.example.inventory.management_product.application.ports.in.ProductUseCase;
import com.example.inventory.management_product.infrastructure.mapper.ProductMapper;

import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    private final ProductUseCase productUseCase;
    private final ProductMapper mapper;

    public ProductController(ProductUseCase productUseCase, ProductMapper mapper) {
        this.productUseCase = productUseCase;
        this.mapper = mapper;
    }

    public Optional<ProductResponse> findByID(String idProduct) {
        var productResponse = productUseCase.findById(idProduct);
        return mapper.toProductResponse(productResponse);
    }
}
