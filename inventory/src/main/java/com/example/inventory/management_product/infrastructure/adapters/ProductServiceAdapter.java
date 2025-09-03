package com.example.inventory.management_product.infrastructure.adapters;

import com.example.inventory.management_product.application.ports.out.ProductRepository;
import com.example.inventory.management_product.domain.models.Product;
import com.example.inventory.management_product.infrastructure.config.ProductServiceProperties;
import com.example.inventory.management_product.infrastructure.dto.LoginResponse;
import com.example.inventory.management_product.infrastructure.dto.ProductResponse;
import com.example.inventory.management_product.infrastructure.mapper.LoginMapper;
import com.example.inventory.management_product.infrastructure.mapper.ProductServiceMapper;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Optional;

@Component
public class ProductServiceAdapter implements ProductRepository {

    private final ProductServiceProperties properties;
    private final RestTemplate restTemplate;
    private String token;

    public ProductServiceAdapter(ProductServiceProperties properties) {
        this.properties = properties;
        this.restTemplate = new RestTemplate();
        login();
    }


    @Override
    @CircuitBreaker(name = "product-service", fallbackMethod = "fallbackFindById")
    public Optional<Product> findById(String id) {
        String url = properties.getProductUrl() + "/products/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<ProductResponse> response =
            restTemplate.exchange(url, HttpMethod.GET, entity, ProductResponse.class);

        Product product = ProductServiceMapper.toDomain(response.getBody());

        return Optional.ofNullable(product);
    }


    @CircuitBreaker(name = "product-service", fallbackMethod = "fallbackLogin")
    private void login() {
        String url = properties.getProductUrl() + "/auth/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = String.format("{\"username\":\"%s\",\"password\":\"%s\"}",
                properties.getProductUser(), properties.getProductPassword());

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<LoginResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, LoginResponse.class);

        this.token = LoginMapper.toToken(response.getBody());
    }

    public void renewToken() {
        login();
    }

    public Optional<Product> fallbackFindById(String id, Throwable ex) {
        System.err.println("Fallback findById: " + ex.getMessage());
        return Optional.empty();
    }

    public String fallbackLogin(Throwable ex) {
        System.err.println("Fallback login: " + ex.getMessage());
        return "dummy-token";
    }
}
