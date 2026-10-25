package com.example.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemRequestDto(
        @Schema(example = "1")
        @NotNull(message = "Book id cannot be null")
        @Positive(message = "Book id must be positive")
        Long bookId,

        @Schema(example = "2")
        @Positive(message = "Quantity must be positive")
        int quantity
) {
}
