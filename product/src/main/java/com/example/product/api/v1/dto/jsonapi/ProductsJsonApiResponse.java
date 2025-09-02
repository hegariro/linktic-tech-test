package com.example.product.api.v1.dto.jsonapi;

import java.util.List;

public record ProductsJsonApiResponse(
    List<ProductResponse> data
) { }
