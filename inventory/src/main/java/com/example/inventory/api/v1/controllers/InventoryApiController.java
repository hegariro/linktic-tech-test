package com.example.inventory.api.v1.controllers;

import java.util.List;
import java.util.Optional;

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

import com.example.inventory.api.v1.dto.BuyProductsJsonApiAttribs;
import com.example.inventory.api.v1.dto.BuyProductsJsonApiRequest;
import com.example.inventory.api.v1.dto.BuyProductsJsonApiResponse;
import com.example.inventory.api.v1.dto.BuyProductsResponse;
import com.example.inventory.api.v1.dto.ErrorResponse;
import com.example.inventory.api.v1.dto.InventoryDomainResponse;
import com.example.inventory.api.v1.dto.InventoryJsonApiResponse;
import com.example.inventory.api.v1.dto.InventoryResponse;
import com.example.inventory.api.v1.dto.ProductResponse;
import com.example.inventory.api.v1.dto.SellProductsJsonApiAttribs;
import com.example.inventory.api.v1.dto.SellProductsJsonApiRequest;
import com.example.inventory.api.v1.dto.SellProductsJsonApiResponse;
import com.example.inventory.api.v1.dto.SellProductsResponse;
import com.example.inventory.management_inventory.infrastructure.InventoryController;
import com.example.inventory.management_product.infrastructure.ProductController;
import com.example.inventory.shared.validators.BuyProductsValidator;
import com.example.inventory.shared.validators.SellProductsValidator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/v1/inventory")
@Tag(name = "Inventario", description = "Operaciones de gestión de inventario, como consultar, comprar y vender productos")
@SecurityRequirement(name = "BearerAuth") 
public class InventoryApiController {

    private final ProductController productController;
    private final InventoryController inventoryController;
    private final BuyProductsValidator buyProductsValidator;
    private final SellProductsValidator sellProductsValidator;

    public InventoryApiController(
        ProductController productController,
        InventoryController inventoryController,
        BuyProductsValidator buyProductsValidator,
        SellProductsValidator sellProductsValidator
    ) {
        this.productController = productController;
        this.inventoryController = inventoryController;
        this.buyProductsValidator = buyProductsValidator;
        this.sellProductsValidator = sellProductsValidator;
    }

    @InitBinder("BuyProductsJsonApiRequest")
    protected void initBuyBinder(WebDataBinder binder) {
        binder.addValidators(buyProductsValidator);
    }

    @InitBinder("SellProductsJsonApiRequest")
    protected void initSellBinder(WebDataBinder binder) {
        binder.addValidators(sellProductsValidator);
    }


    @Operation(
        summary = "Obtiene el inventario de un producto por ID",
        description = "Busca el inventario disponible de un producto específico usando su ID único."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Inventario del producto encontrado",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = InventoryJsonApiResponse.class)
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error de validación o inventario no encontrado",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)
        )
    )
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

    @Operation(
        summary = "Añade productos al inventario (compra)",
        description = "Permite registrar la compra de uno o varios productos, incrementando la cantidad en el inventario."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Lista de productos a comprar, siguiendo el estándar JSON:API.",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = BuyProductsJsonApiRequest.class)
        )
    )
    @ApiResponse(
        responseCode = "200",
        description = "Compra procesada exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = BuyProductsJsonApiResponse.class)
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error de validación o del proceso de compra",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)
        )
    )
    @PostMapping("/products/buy")
    public ResponseEntity<?> buyProducts(@Valid @RequestBody BuyProductsJsonApiRequest request) {
        try {
            BuyProductsJsonApiAttribs attribs = request.data().attributes();
            Optional<BuyProductsResponse> buyResponse = inventoryController.buyProducts(attribs);

            if (!buyResponse.isPresent()) {
                throw new Exception("Unknown error");
            }

            BuyProductsJsonApiResponse response = BuyProductsResponse.fromDomain(buyResponse.get());
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

    @Operation(
        summary = "Vende productos del inventario",
        description = "Permite registrar la venta de uno o varios productos, reduciendo la cantidad en el inventario."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Lista de productos a vender, siguiendo el estándar JSON:API.",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = SellProductsJsonApiRequest.class)
        )
    )
    @ApiResponse(
        responseCode = "200",
        description = "Venta procesada exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = SellProductsJsonApiResponse.class)
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error de validación, stock insuficiente o del proceso de venta",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)
        )
    )
    @PostMapping("/products/sell")
    public ResponseEntity<?> sellProducts(@Valid @RequestBody SellProductsJsonApiRequest request) {
        try {
            SellProductsJsonApiAttribs attribs = request.data().attributes();
            Optional<SellProductsResponse> sellResponse = inventoryController.sellProducts(attribs);

            if (!sellResponse.isPresent()) {
                throw new Exception("Unknown error");
            }

            SellProductsJsonApiResponse response = SellProductsResponse.fromDomain(sellResponse.get());
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
