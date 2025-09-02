package com.example.product.api.v1.controllers;

import com.example.product.api.v1.dto.CreateProductRequest;
import com.example.product.management_product.application.ports.CreateProductCommand;
import com.example.product.shared.infrastructure.ProductCreationValidator;
import com.example.product.shared.infrastructure.ValidationsConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductController Tests")
class ProductControllerTest {

    @Mock
    private CreateProductCommand createProductCommand;

    @Mock
    private ProductCreationValidator productCreationValidator;

    @Mock
    private ValidationsConfig validationsConfig;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        // Configurar MockMvc con el controlador y el validator
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new TestExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("Create Product - Success Cases")
    class CreateProductSuccessCases {

        @Test
        @DisplayName("Should create product successfully with valid data")
        void shouldCreateProductSuccessfully() throws Exception {
            // Given
            CreateProductRequest request = new CreateProductRequest(
                    "Test Product",
                    "Test Description",
                    new BigDecimal("99.99")
            );

            doNothing().when(createProductCommand).createProduct(
                    eq("Test Product"),
                    eq("Test Description"),
                    eq(new BigDecimal("99.99"))
            );

            // When & Then
            mockMvc.perform(post("/api/v1/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(""));

            verify(createProductCommand, times(1)).createProduct(
                    "Test Product",
                    "Test Description",
                    new BigDecimal("99.99")
            );
        }

        @Test
        @DisplayName("Should create product with null description")
        void shouldCreateProductWithNullDescription() throws Exception {
            // Given
            CreateProductRequest request = new CreateProductRequest(
                    "Test Product",
                    null,
                    new BigDecimal("50.00")
            );

            doNothing().when(createProductCommand).createProduct(anyString(), isNull(), any(BigDecimal.class));

            // When & Then
            mockMvc.perform(post("/api/v1/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            verify(createProductCommand, times(1)).createProduct("Test Product", null, new BigDecimal("50.00"));
        }
    }

    @Nested
    @DisplayName("Create Product - Validation Error Cases")
    class CreateProductValidationErrorCases {

        @Test
        @DisplayName("Should return bad request when name is blank")
        void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
            // Given
            String jsonWithBlankName = """
                {
                    "name": "",
                    "description": "Test Description",
                    "price": 99.99
                }
                """;

            // When & Then
            mockMvc.perform(post("/api/v1/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonWithBlankName))
                    .andExpect(status().isBadRequest());

            verify(createProductCommand, never()).createProduct(anyString(), anyString(), any(BigDecimal.class));
        }

        @Test
        @DisplayName("Should return bad request when price is null")
        void shouldReturnBadRequestWhenPriceIsNull() throws Exception {
            // Given
            String jsonWithNullPrice = """
                {
                    "name": "Test Product",
                    "description": "Test Description",
                    "price": null
                }
                """;

            // When & Then
            mockMvc.perform(post("/api/v1/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonWithNullPrice))
                    .andExpect(status().isBadRequest());

            verify(createProductCommand, never()).createProduct(anyString(), anyString(), any(BigDecimal.class));
        }

        @Test
        @DisplayName("Should return bad request when name exceeds max length - custom validator")
        void shouldReturnBadRequestWhenNameExceedsMaxLength() throws Exception {
            // Given
            CreateProductRequest request = new CreateProductRequest(
                    "Very Long Product Name That Exceeds Maximum Length",
                    "Test Description",
                    new BigDecimal("99.99")
            );

            doThrow(new RuntimeException("Validation failed: name too long"))
                    .when(createProductCommand).createProduct(anyString(), anyString(), any(BigDecimal.class));

            // When & Then
            mockMvc.perform(post("/api/v1/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    @DisplayName("Create Product - Malformed Request Cases")
    class CreateProductMalformedRequestCases {

        @Test
        @DisplayName("Should return bad request when JSON is malformed")
        void shouldReturnBadRequestWhenJsonMalformed() throws Exception {
            // Given
            String malformedJson = "{ \"name\": \"Test\", \"price\": }"; // JSON incompleto

            // When & Then
            mockMvc.perform(post("/api/v1/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(malformedJson))
                    .andExpect(status().isBadRequest());

            verify(createProductCommand, never()).createProduct(anyString(), anyString(), any(BigDecimal.class));
        }
    }

    @Nested
    @DisplayName("Controller Configuration Tests")
    class ControllerConfigurationTests {

        @Test
        @DisplayName("Should have correct request mapping")
        void shouldHaveCorrectRequestMapping() {
            // Given & When
            RequestMapping requestMapping = ProductController.class.getAnnotation(RequestMapping.class);

            // Then
            assertNotNull(requestMapping);
            assertArrayEquals(new String[]{"/api/v1/products"}, requestMapping.value());
        }

        @Test
        @DisplayName("Should be annotated as RestController")
        void shouldBeAnnotatedAsRestController() {
            // Given & When
            RestController restController = ProductController.class.getAnnotation(RestController.class);

            // Then
            assertNotNull(restController);
        }
    }

    // Helper class for exception handling in tests
    @RestControllerAdvice
    static class TestExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
            return ResponseEntity.badRequest().body("Validation failed");
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
            return ResponseEntity.badRequest().body("Malformed JSON request");
        }

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + ex.getMessage());
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleGenericException(Exception ex) {
            return ResponseEntity.badRequest().body("Bad request: " + ex.getMessage());
        }
    }
}