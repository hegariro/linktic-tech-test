package com.example.inventory.api.v1.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.inventory.api.v1.dto.BuyProductsJsonApiAttribs;
import com.example.inventory.api.v1.dto.BuyProductsResponse;
import com.example.inventory.api.v1.dto.InventoryDomainResponse;
import com.example.inventory.api.v1.dto.ProductResponse;
import com.example.inventory.api.v1.dto.SellProductsJsonApiAttribs;
import com.example.inventory.api.v1.dto.SellProductsResponse;
import com.example.inventory.management_inventory.infrastructure.InventoryController;
import com.example.inventory.management_product.infrastructure.ProductController;
import com.example.inventory.shared.validators.BuyProductsValidator;
import com.example.inventory.shared.validators.SellProductsValidator;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests unitarios para {@link InventoryApiController}.
 * 
 * <p>Esta clase verifica el comportamiento del controlador API de inventario,
 * incluyendo operaciones de consulta, compra y venta de productos.</p>
 * 
 * <p>Los tests están organizados en clases anidadas para mejorar la organización
 * y legibilidad, agrupando tests por funcionalidad.</p>
 */
@WebMvcTest(
    controllers = {InventoryApiController.class},
    excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
@DisplayName("Inventory API Controller Tests")
class InventoryApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductController productController;

    @MockBean
    private InventoryController inventoryController;

    @MockBean
    private BuyProductsValidator buyProductsValidator;

    @MockBean
    private SellProductsValidator sellProductsValidator;

    private static final String BASE_URL = "/v1/inventory";
    
    private ProductResponse sampleProduct;
    private InventoryDomainResponse sampleInventory;

    /**
     * Configuración inicial para cada test.
     * Prepara objetos de prueba comunes utilizados en múltiples tests.
     */
    @BeforeEach
    void setUp() {
        sampleProduct = new ProductResponse(
            "product-123", 
            "Test Product", 
            "A sample product for testing", 
            new BigDecimal("29000.00")
        );

        sampleInventory = new InventoryDomainResponse(
            "inventory-456",
            "product-123",
            100
        );
    }

    /**
     * Tests para el endpoint GET /v1/inventory/product/{idProduct}
     */
    @Nested
    @DisplayName("Get Product Inventory Tests")
    class GetProductInventoryTests {

        /**
         * Verifica que se retorne el inventario cuando el producto existe.
         */
        @Test
        @DisplayName("Should return inventory when product exists")
        void shouldReturnInventoryWhenProductExists() throws Exception {
            // Given
            String productId = "product-123";
            
            when(productController.findByID(productId))
                .thenReturn(Optional.of(sampleProduct));
            when(inventoryController.findByIdProduct(productId))
                .thenReturn(Optional.of(sampleInventory));

            // When & Then
            mockMvc.perform(get(BASE_URL + "/product/{idProduct}", productId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.data.type").value("inventory"))
                    .andExpect(jsonPath("$.data.id").value(sampleInventory.id()))
                    .andExpect(jsonPath("$.data.attributes.productId").value(productId))
                    .andExpect(jsonPath("$.data.attributes.quantityAvailable").value(100))
                    .andExpect(jsonPath("$.data.attributes.product.name").value(sampleProduct.name()));
        }

        /**
         * Verifica que se retorne error 400 cuando el producto no existe.
         */
        @Test
        @DisplayName("Should return 400 when product not found")
        void shouldReturn400WhenProductNotFound() throws Exception {
            // Given
            String productId = "non-existent-product";
            
            when(productController.findByID(productId))
                .thenReturn(Optional.empty());

            // When & Then
            mockMvc.perform(get(BASE_URL + "/product/{idProduct}", productId))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.errors").isArray())
                    .andExpect(jsonPath("$.errors[0].status").value("400 BAD_REQUEST"))
                    .andExpect(jsonPath("$.errors[0].detail").value("Product " + productId + " not found"));
        }

        /**
         * Verifica que se retorne error 400 cuando el producto existe pero no tiene inventario.
         */
        @Test
        @DisplayName("Should return 400 when product has no inventory")
        void shouldReturn400WhenProductHasNoInventory() throws Exception {
            // Given
            String productId = "product-no-inventory";
            
            when(productController.findByID(productId))
                .thenReturn(Optional.of(sampleProduct));
            when(inventoryController.findByIdProduct(productId))
                .thenReturn(Optional.empty());

            // When & Then
            mockMvc.perform(get(BASE_URL + "/product/{idProduct}", productId))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.errors").isArray())
                    .andExpect(jsonPath("$.errors[0].detail").value("Product's Inventory " + productId + " void"));
        }
    }

    /**
     * Tests para el endpoint POST /v1/inventory/products/buy
     */
    @Nested
    @DisplayName("Buy Products Tests")
    class BuyProductsTests {

        /**
         * Verifica que la compra de productos sea exitosa con datos válidos.
         */
        @Test
        @DisplayName("Should process buy request successfully")
        void shouldProcessBuyRequestSuccessfully() throws Exception {
            // Given
            String requestBody = """
                {
                    "data": {
                        "id": "any-id",
                        "type": "buyProducts",
                        "attributes": {
                            "total": 110000.00
                            "items": [
                                {
                                    "productId": "product-123",
                                    "quantity": 50,
                                    "subtotal": 110000.00
                                }
                            ]
                        }
                    }
                }
                """;

            BuyProductsResponse buyResponse = new BuyProductsResponse(
                "transaction-123",
                50,
                new BigDecimal("1499.50"),
                List.of("product-123")
            );

            when(inventoryController.buyProducts(any(BuyProductsJsonApiAttribs.class)))
                .thenReturn(Optional.of(buyResponse));

            // When & Then
            mockMvc.perform(post(BASE_URL + "/products/buy")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.data.type").value("inbound"))
                    .andExpect(jsonPath("$.data.id").value("transaction-123"))
                    // .andExpect(jsonPath("$.data.attributes.totalItemsOk").value(50))
                    // .andExpect(jsonPath("$.data.attributes.total").value(1499.50))
                    .andExpect(jsonPath("$.data.attributes.idItemsList[0]").value("product-123"));
        }

        /**
         * Verifica que se retorne error cuando falla la compra.
         */
        @Test
        @DisplayName("Should return error when buy fails")
        void shouldReturnErrorWhenBuyFails() throws Exception {
            // Given
            String requestBody = """
                {
                    "data": {
                        "type": "buy-products",
                        "attributes": {
                            "items": [
                                {
                                    "productId": "invalid-product",
                                    "quantity": 50
                                }
                            ]
                        }
                    }
                }
                """;

            when(inventoryController.buyProducts(any(BuyProductsJsonApiAttribs.class)))
                .thenReturn(Optional.empty());

            // When & Then
            mockMvc.perform(post(BASE_URL + "/products/buy")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.errors").isArray())
                    .andExpect(jsonPath("$.errors[0].detail").value("Unknown error"));
        }

        /**
         * Verifica que se valide correctamente el formato JSON:API.
         */
        @Test
        @DisplayName("Should validate JSON:API format")
        void shouldValidateJsonApiFormat() throws Exception {
            // Given - Request sin el wrapper "data"
            String invalidRequestBody = """
                {
                    "items": [
                        {
                            "productId": "product-123",
                            "quantity": 50
                        }
                    ]
                }
                """;

            // When & Then
            mockMvc.perform(post(BASE_URL + "/products/buy")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidRequestBody))
                    .andExpect(status().isBadRequest());
        }
    }

    /**
     * Tests para el endpoint POST /v1/inventory/products/sell
     */
    @Nested
    @DisplayName("Sell Products Tests")
    class SellProductsTests {

        /**
         * Verifica que la venta de productos sea exitosa con datos válidos.
         */
        @Test
        @DisplayName("Should process sell request successfully")
        void shouldProcessSellRequestSuccessfully() throws Exception {
            // Given
            String requestBody = """
                {
                    "data": {
                        "type": "sell-products",
                        "attributes": {
                            "items": [
                                {
                                    "productId": "product-123",
                                    "quantity": 25
                                }
                            ]
                        }
                    }
                }
                """;

            SellProductsResponse sellResponse = new SellProductsResponse(
                "transaction-456",
                25,
                new BigDecimal("749.75"),
                List.of("product-123")
            );

            when(inventoryController.sellProducts(any(SellProductsJsonApiAttribs.class)))
                .thenReturn(Optional.of(sellResponse));

            // When & Then
            mockMvc.perform(post(BASE_URL + "/products/sell")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.data.type").value("sell-products"))
                    .andExpect(jsonPath("$.data.id").value("transaction-456"))
                    .andExpect(jsonPath("$.data.attributes.totalItemsOk").value(25))
                    .andExpect(jsonPath("$.data.attributes.total").value(749.75));
        }

        /**
         * Verifica que se retorne error cuando no hay stock suficiente.
         */
        @Test
        @DisplayName("Should return error when insufficient stock")
        void shouldReturnErrorWhenInsufficientStock() throws Exception {
            // Given
            String requestBody = """
                {
                    "data": {
                        "type": "sell-products",
                        "attributes": {
                            "items": [
                                {
                                    "productId": "product-123",
                                    "quantity": 1000
                                }
                            ]
                        }
                    }
                }
                """;

            when(inventoryController.sellProducts(any(SellProductsJsonApiAttribs.class)))
                .thenReturn(Optional.empty());

            // When & Then
            mockMvc.perform(post(BASE_URL + "/products/sell")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.errors").isArray())
                    .andExpect(jsonPath("$.errors[0].detail").value("Unknown error"));
        }

        /**
         * Verifica que cantidades negativas o cero sean rechazadas.
         */
        @Test
        @DisplayName("Should reject negative or zero quantities")
        void shouldRejectNegativeOrZeroQuantities() throws Exception {
            // Given
            String requestBody = """
                {
                    "data": {
                        "type": "sell-products",
                        "attributes": {
                            "items": [
                                {
                                    "productId": "product-123",
                                    "quantity": 0
                                }
                            ]
                        }
                    }
                }
                """;

            // When & Then - Debería ser rechazado por validación
            mockMvc.perform(post(BASE_URL + "/products/sell")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().isBadRequest());
        }
    }

    /**
     * Tests generales de validación y manejo de errores.
     */
    @Nested
    @DisplayName("General Validation Tests")
    class GeneralValidationTests {

        /**
         * Verifica que requests con JSON malformado sean rechazadas.
         */
        @Test
        @DisplayName("Should reject malformed JSON")
        void shouldRejectMalformedJson() throws Exception {
            // Given
            String malformedJson = "{ invalid json structure }";

            // When & Then
            mockMvc.perform(post(BASE_URL + "/products/buy")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(malformedJson))
                    .andExpect(status().isBadRequest());
        }

        /**
         * Verifica que se requiera el Content-Type correcto.
         */
        @Test
        @DisplayName("Should require correct Content-Type")
        void shouldRequireCorrectContentType() throws Exception {
            // Given
            String validJson = """
                {
                    "data": {
                        "type": "buy-products",
                        "attributes": {
                            "items": []
                        }
                    }
                }
                """;

            // When & Then
            mockMvc.perform(post(BASE_URL + "/products/buy")
                    .contentType(MediaType.TEXT_PLAIN) // Content-Type incorrecto
                    .content(validJson))
                    .andExpect(status().isUnsupportedMediaType());
        }
    }
}
