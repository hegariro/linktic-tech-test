package com.example.product.management_product.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class ProductEntity {

    @Id
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;

    // Este constructor se usa para crear una nueva instancia a partir de la entidad del dominio
    public ProductEntity(UUID id, String name, String description, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
