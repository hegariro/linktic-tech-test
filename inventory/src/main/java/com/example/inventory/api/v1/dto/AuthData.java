package com.example.inventory.api.v1.dto;

import java.util.UUID;

public record AuthData(String token) {

    public static JsonApiData<AuthData> fromDomain(String token) {

        return new JsonApiData<>(
            UUID.randomUUID().toString(),
            "auth",
            new AuthData(token)
        );
    }
}
