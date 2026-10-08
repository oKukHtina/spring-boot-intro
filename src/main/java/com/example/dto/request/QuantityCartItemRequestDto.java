package com.example.dto.request;

import jakarta.validation.constraints.Min;

public record QuantityCartItemRequestDto(
        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity
) {
}
