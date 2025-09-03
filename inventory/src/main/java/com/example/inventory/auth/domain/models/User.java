package com.example.inventory.auth.domain.models;

public record User(
    String id, String nickname, String name, String token
) { }
