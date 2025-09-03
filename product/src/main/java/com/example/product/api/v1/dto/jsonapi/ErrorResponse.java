package com.example.product.api.v1.dto.jsonapi;

import java.util.List;

public record ErrorResponse(
    List<ErrorResponseAttributes> errors
) {
    public record ErrorResponseAttributes(
        String status,
        String title,
        String detail
    ) { }
}
