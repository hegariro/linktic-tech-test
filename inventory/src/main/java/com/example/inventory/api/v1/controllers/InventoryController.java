package com.example.inventory.api.v1.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.inventory.api.v1.dto.*;
import com.example.inventory.management_product.infrastructure.ProductController;

@RestController
@RequestMapping("/v1/inventory")
public class InventoryController {
    
    private final ProductController productController;

    public InventoryController(ProductController productController) {
        this.productController = productController;
    }

    @GetMapping("/product/{idProduct}")
    public ResponseEntity<?> getProductByID(@PathVariable String idProduct) {
        try {
            Optional<ProductResponse> productResponse = productController.findByID(idProduct);
            if (productResponse.isPresent()) {
                ProductResponse product = productResponse.get();
                InventoryDomainResponse domain = inventoryCommand.getInventoryByProduct(product.id());
                InventoryJsonApiResponse response = InventoryResponse.fromDomain(product, domain);

                return ResponseEntity.ok().body(response);
            } else {
                throw new Exception("Product "+idProduct+" not found");
            }
        } catch(Exception e) {
            ErrorResponse response = new ErrorResponse(List.of(
            new ErrorResponse.ErrorResponseAttributes(
                HttpStatusCode.valueOf(400).toString(),
                "Error inesperado",
                e.getMessage()
            )
        ));
        }
    }
}
