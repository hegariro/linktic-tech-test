package com.example.inventory.api.v1.dto;

public record InventoryDomainResponse(
    String id,
    String idProduct,
    int quantityAvailable
) { }
