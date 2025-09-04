package com.example.inventory.management_inventory.domain.models;

public record Inventory(
    String id, String idProduct, int quantityAvailable
) { }
