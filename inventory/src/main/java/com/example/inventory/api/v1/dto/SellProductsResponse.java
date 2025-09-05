package com.example.inventory.api.v1.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SellProductsResponse(
    @NotBlank String id,
    @NotNull @Positive int totalItems,
    @NotNull @Positive BigDecimal total,
    List<String> idItemList
) {
    public static SellProductsJsonApiResponse fromDomain(SellProductsResponse attribs) {
        return new SellProductsJsonApiResponse(
            new JsonApiData<>(
                attribs.id(), 
                "outbound",
                new SellProductsResponse(
                    attribs.id(), 
                    attribs.totalItems(), 
                    attribs.total(),
                    attribs.idItemList()
                )
            )
        );
    }
}
