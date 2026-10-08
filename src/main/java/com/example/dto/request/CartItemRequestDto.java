package com.example.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemRequestDto(
        @NotNull(message = "Book id cannot be null")
        Long bookId,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity
) {
}
