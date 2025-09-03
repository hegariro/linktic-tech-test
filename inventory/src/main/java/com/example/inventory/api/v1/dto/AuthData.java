package com.example.inventory.api.v1.dto;

public record AuthData(String token) {
    public static AuthData fromDomain(String token) {
        return new AuthData(token);
    }
}
