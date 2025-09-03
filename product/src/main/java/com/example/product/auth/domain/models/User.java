package com.example.product.auth.domain.models;

public record User(String id, String nickname, String name) {
    public static User create(String id, String nickname, String name) {
        return new User(id, nickname, name);
    }
}
