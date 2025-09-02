package com.example.product.api.v1.controllers;

import com.example.product.management_product.domain.models.Product;
import com.example.product.api.v1.dto.CreateProductRequest;
import com.example.product.management_product.application.ports.CreateProductCommand;
import com.example.product.shared.infrastructure.ProductCreationValidator;
import com.example.product.api.v1.dto.jsonapi.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    private final CreateProductCommand createProductCommand;
    private final ProductCreationValidator productCreationValidator;

    public ProductController(
            CreateProductCommand createProductCommand,
            ProductCreationValidator productCreationValidator
    ) {
        this.createProductCommand = createProductCommand;
        this.productCreationValidator = productCreationValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(productCreationValidator);
    }

    @PostMapping
    public ResponseEntity<ProductJsonApiResponse> createProduct(
            @Valid @RequestBody CreateProductJsonApiRequest request
    ) {
        CreateProductAttributes attributes = request.data().attributes();
        Product product = createProductCommand.createProduct(
                attributes.name(), attributes.description(), attributes.price());

        ProductJsonApiResponse response = new ProductJsonApiResponse(ProductResponse.fromDomain(product));
        return new ResponseEntity(response, HttpStatus.CREATED);
    }
}
