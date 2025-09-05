package com.example.inventory.api.v1.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record SellProductsJsonApiRequest(
    @NotNull @Valid JsonApiData<SellProductsJsonApiAttribs> data
) { }
