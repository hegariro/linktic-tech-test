package com.example.product.api.v1.dto.jsonapi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record JsonApiData<T>(
        @NotBlank String type,
        @NotNull T attributes
) {}