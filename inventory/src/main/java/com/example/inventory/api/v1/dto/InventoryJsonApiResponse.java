package com.example.inventory.api.v1.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.List;

public record InventoryJsonApiResponse(
    @NotNull @Valid List<JsonApiDataWithRelationship<ProductDataResponse, RelationshipsResponse>> data,
    List<JsonApiData<InventoryData>> included
) {

    public record ProductDataResponse(
        @NotNull ProductAttributes attributes
    ) {
        public static ProductDataResponse fromDomain(ProductResponse data) {
            return new ProductDataResponse(
                new ProductAttributes(
                    data.name(),
                    data.description(),
                    data.price()
                )
            );
        }
    }

    public record InventoryData(
        @NotBlank String idProduct,
        @NotNull @Positive int quantityAvalaible
    ) {
        public static InventoryData fromDomain(InventoryDomainResponse data) {
            return new InventoryData(data.idProduct(), data.quantityAvailable());
        }
    }
}
