package com.example.product.api.v1.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.product.api.v1.dto.jsonapi.CreateProductAttributes;
import com.example.product.api.v1.dto.jsonapi.CreateProductJsonApiRequest;
import com.example.product.api.v1.dto.jsonapi.UpdateProductRequest;
import com.example.product.api.v1.dto.jsonapi.ErrorResponse;
import com.example.product.api.v1.dto.jsonapi.ProductJsonApiResponse;
import com.example.product.api.v1.dto.jsonapi.ProductResponse;
import com.example.product.api.v1.dto.jsonapi.ProductsJsonApiResponse;
import com.example.product.management_product.application.ports.ProductCommand;
import com.example.product.management_product.domain.models.Product;
import com.example.product.shared.infrastructure.ProductCreationValidator;
import com.example.product.shared.infrastructure.UpdateProductValidator;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductCommand productCommand;
    private final ProductCreationValidator productCreationValidator;
    private final UpdateProductValidator updateProductValidator;

    public ProductController(
            ProductCommand productCommand,
            ProductCreationValidator productCreationValidator,
            UpdateProductValidator updateProductValidator
    ) {
        this.productCommand = productCommand;
        this.productCreationValidator = productCreationValidator;
        this.updateProductValidator = updateProductValidator;
    }

    @InitBinder("createProductJsonApiRequest")
    protected void initCreateBinder(WebDataBinder binder) {
        binder.addValidators(productCreationValidator);
    }

    @InitBinder("updateProductRequest")
    protected void initUpdateBinder(WebDataBinder binder) {
        binder.addValidators(updateProductValidator);
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

    @PatchMapping("/{idProduct}")
    public ResponseEntity<?> updateProduct(@PathVariable String idProduct, @Valid @RequestBody UpdateProductRequest request) {
        try {
            var attribs = request.data();
            Product product = productCommand.getProductByID(idProduct);
            Product updatedProduct = productCommand.updateProduct(
                product.id(), attribs.name(), attribs.description(), attribs.price()
            );

            ProductJsonApiResponse response = new ProductJsonApiResponse(ProductResponse.fromDomain(updatedProduct));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            ErrorResponse response = new ErrorResponse(List.of(new ErrorResponse
                .ErrorResponseAttributes(
                    HttpStatusCode.valueOf(404).toString(),
                    "Product not found",
                    e.getMessage()
                )
            ));
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(404));
        }
    }

    @DeleteMapping("/{idProduct}")
    public ResponseEntity<?> deleteProduct(@PathVariable String idProduct) {
        try {
            productCommand.deleteProduct(idProduct);
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(204));
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
}
