package com.example.inventory.api.v1.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record BuyProductsJsonApiRequest(
    @NotNull @Valid JsonApiData<BuyProductsJsonApiAttribs> data
) { }
