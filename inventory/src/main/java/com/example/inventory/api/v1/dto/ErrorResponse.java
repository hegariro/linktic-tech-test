package com.example.inventory.api.v1.dto;

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
