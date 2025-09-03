package com.example.inventory.api.v1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record JsonApiDataWithRelationship<T, U>(
   @NotBlank String id,
   @NotBlank String type,
   @NotNull T attributes,
   @NotNull U relationships
) { }