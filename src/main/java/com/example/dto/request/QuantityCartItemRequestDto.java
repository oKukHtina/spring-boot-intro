package com.example.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

public record QuantityCartItemRequestDto(
        @Schema(example = "1")
        @Positive(message = "Quantity must be positive")
        int quantity
) {
}
