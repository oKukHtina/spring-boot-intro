package com.example.dto.request;

import jakarta.validation.constraints.Positive;

public record QuantityCartItemRequestDto(
        @Positive(message = "Quantity must be positive")
        int quantity
) {
}
