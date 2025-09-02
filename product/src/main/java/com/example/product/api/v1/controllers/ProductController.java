package com.example.product.api.v1.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.product.api.v1.dto.jsonapi.CreateProductAttributes;
import com.example.product.api.v1.dto.jsonapi.CreateProductJsonApiRequest;
import com.example.product.api.v1.dto.jsonapi.ErrorResponse;
import com.example.product.api.v1.dto.jsonapi.ProductJsonApiResponse;
import com.example.product.api.v1.dto.jsonapi.ProductResponse;
import com.example.product.api.v1.dto.jsonapi.ProductsJsonApiResponse;
import com.example.product.management_product.application.ports.ProductCommand;
import com.example.product.management_product.domain.models.Product;
import com.example.product.shared.infrastructure.ProductCreationValidator;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductCommand productCommand;
    private final ProductCreationValidator productCreationValidator;

    public ProductController(
            ProductCommand productCommand,
            ProductCreationValidator productCreationValidator
    ) {
        this.productCommand = productCommand;
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
        Product product = productCommand.createProduct(
                attributes.name(), attributes.description(), attributes.price());

        ProductJsonApiResponse response = new ProductJsonApiResponse(ProductResponse.fromDomain(product));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{idProduct}")
    public ResponseEntity<?> getProductByID(@PathVariable String idProduct) {
        try {
            Product product = productCommand.getProductByID(idProduct);

            ProductJsonApiResponse response = new ProductJsonApiResponse(ProductResponse.fromDomain(product));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
                ErrorResponse response = new ErrorResponse(List.of(new ErrorResponse.ErrorResponseAttributes(
                    HttpStatusCode.valueOf(404).toString(),
                    "Product not found",
                    e.getMessage()
                )
            ));
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(404));
        }
    }
    
    @GetMapping("/")
    public ResponseEntity<?> getProducts() {
        try {
            List<Product> products = productCommand.getProducts();

            ProductsJsonApiResponse response = new ProductsJsonApiResponse(ProductResponse.fromDomain(products));
            return new ResponseEntity<>(response, HttpStatus.OK);   
        } catch (Exception e) {
                ErrorResponse response = new ErrorResponse(List.of(new ErrorResponse.ErrorResponseAttributes(
                    HttpStatusCode.valueOf(404).toString(),
                    "empty database",
                    e.getMessage()
                )
            ));
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(404));
        }
    }
    
}
