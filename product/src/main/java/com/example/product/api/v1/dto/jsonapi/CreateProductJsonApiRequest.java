package com.example.product.api.v1.dto.jsonapi;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreateProductJsonApiRequest(
        @NotNull @Valid JsonApiData<CreateProductAttributes> data
) {}