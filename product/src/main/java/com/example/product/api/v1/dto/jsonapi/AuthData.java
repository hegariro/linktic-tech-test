package com.example.product.api.v1.dto.jsonapi;

public record AuthData(String token) {
    public static AuthData fromDomain(String token) {
        return new AuthData(token);
    }
}
