package com.example.inventory.api.v1.dto;

import jakarta.validation.constraints.*;

public record RelationshipsResponse(@NotNull Inventory inventory) {
    public record Inventory(@NotNull Data data) {
        public record Data(
            @NotNull String id,
            @NotBlank String type
        ) {}
    }
}
