package com.example.inventory.api.v1.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BuyProductsResponse(
    @NotBlank String id,
    @NotNull @Positive int totalItems,
    @NotNull @Positive BigDecimal total,
    List<String> idItemList
) {
    public static BuyProductsJsonApiResponse fromDomain(BuyProductsResponse attribs) {
        return new BuyProductsJsonApiResponse(
            new JsonApiData<>(
                attribs.id(), 
                "inbound",
                new BuyProductsResponse(
                    attribs.id(), 
                    attribs.totalItems(), 
                    attribs.total(),
                    attribs.idItemList()
                )
            )
        );
    }
}
