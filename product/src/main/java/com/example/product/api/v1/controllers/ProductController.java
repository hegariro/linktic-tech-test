package com.example.product.api.v1.controllers;

// imports 

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
import com.example.product.api.v1.dto.jsonapi.ErrorResponse;
import com.example.product.api.v1.dto.jsonapi.ProductJsonApiResponse;
import com.example.product.api.v1.dto.jsonapi.ProductResponse;
import com.example.product.api.v1.dto.jsonapi.ProductsJsonApiResponse;
import com.example.product.api.v1.dto.jsonapi.UpdateProductRequest;
import com.example.product.management_product.application.ports.ProductCommand;
import com.example.product.management_product.domain.models.Product;
import com.example.product.shared.infrastructure.ProductCreationValidator;
import com.example.product.shared.infrastructure.UpdateProductValidator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/v1/products")
@Tag(name = "Productos", description = "Operaciones para la gestión de productos")
@SecurityRequirement(name = "BearerAuth")
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

    // OpenAPI Docs
    @Operation(
        summary = "Crea un nuevo producto",
        description = "Crea un nuevo registro de producto con un nombre, descripción y precio."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Datos del producto a crear, siguiendo el estándar JSON:API.",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = CreateProductJsonApiRequest.class)
        )
    )
    @ApiResponse(
        responseCode = "201",
        description = "Producto creado exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ProductJsonApiResponse.class)
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Solicitud inválida",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)
        )
    )
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

    // OpenAPI Docs
    @Operation(
        summary = "Obtiene un producto por ID",
        description = "Busca y devuelve los detalles de un producto específico usando su ID único."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Producto encontrado",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ProductJsonApiResponse.class)
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Producto no encontrado",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)
        )
    )
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

    // OpenAPI Docs
    @Operation(
        summary = "Obtiene todos los productos",
        description = "Devuelve una lista de todos los productos disponibles en el sistema."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de productos obtenida",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ProductsJsonApiResponse.class)
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "La base de datos está vacía",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)
        )
    )
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

    // OpenAPI Docs
    @Operation(
        summary = "Actualiza un producto existente",
        description = "Modifica los atributos de un producto específico, como el nombre, la descripción o el precio."
    )
    @ApiResponse(
        responseCode = "201",
        description = "Producto actualizado exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ProductJsonApiResponse.class)
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Producto no encontrado",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)
        )
    )
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

    // OpenAPI Docs
    @Operation(
        summary = "Elimina un producto",
        description = "Elimina un producto del inventario usando su ID."
    )
    @ApiResponse(
        responseCode = "204",
        description = "Producto eliminado exitosamente (sin contenido de respuesta)"
    )
    @ApiResponse(
        responseCode = "404",
        description = "Producto no encontrado",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)
        )
    )
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
