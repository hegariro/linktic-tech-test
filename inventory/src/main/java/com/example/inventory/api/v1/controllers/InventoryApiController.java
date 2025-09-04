package com.example.inventory.api.v1.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.inventory.api.v1.dto.*;
import com.example.inventory.management_product.infrastructure.ProductController;
import com.example.inventory.management_inventory.infrastructure.InventoryController;

@RestController
@RequestMapping("/v1/inventory")
public class InventoryApiController {
    
    private final ProductController productController;
    private final InventoryController inventoryController;

    public InventoryApiController(
        ProductController productController,
        InventoryController inventoryController
    ) {
        this.productController = productController;
        this.inventoryController = inventoryController;
    }

    @GetMapping("/product/{idProduct}")
    public ResponseEntity<?> getProductByID(@PathVariable String idProduct) {
        try {
            Optional<ProductResponse> productResponse = productController.findByID(idProduct);
            if (!productResponse.isPresent()) {
                throw new Exception("Product "+idProduct+" not found");
            }

            ProductResponse product = productResponse.get();
            Optional<InventoryDomainResponse> domainResponse = inventoryController.findByIdProduct(product.id());

            if (!domainResponse.isPresent()) {
                throw new Exception("Product's Inventory "+idProduct+" void");
            }

            InventoryDomainResponse domain = domainResponse.get();
            InventoryJsonApiResponse response = InventoryResponse.fromDomain(product, domain);

            return ResponseEntity.ok().body(response);
        } catch(Exception e) {
            ErrorResponse response = new ErrorResponse(List.of(
                new ErrorResponse.ErrorResponseAttributes(
                    HttpStatusCode.valueOf(400).toString(),
                    "Error inesperado",
                    e.getMessage()
                )
            ));
            return ResponseEntity.status(400).body(response);
        }
    }
}
