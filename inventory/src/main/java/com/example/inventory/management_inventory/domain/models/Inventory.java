package com.example.inventory.management_inventory.domain.models;

import java.util.UUID;

public record Inventory(
    String id, String idProduct, int quantityAvailable
) {
    public Inventory(String idProduct, int quantityAvailable) {
        this(UUID.randomUUID().toString(), idProduct, quantityAvailable);
    }

    public boolean isQuantityAvailable(String idProduct, int quantity) {
        return this.idProduct.equals(idProduct) && quantity <= this.quantityAvailable;
    }
}
