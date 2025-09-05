package com.example.inventory.management_inventory.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
public class InventoryEntity {

    @Id
    @Column(length = 36)
    private String id;
    @Column(name = "product_id", length = 36)
    private String productId;
    @Column(name = "quantity_available")
    private int quantityAvailable;

    public InventoryEntity(String id, String productId, int quantityAvailable) {
        this.id = id;
        this.productId = productId;
        this.quantityAvailable = quantityAvailable;
    }
}
