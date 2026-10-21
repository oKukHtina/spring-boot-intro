package com.example.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrderItemResponseDto(
        @Schema(example = "1")
        Long id,
        @Schema(example = "3")
        Long bookId,
        @Schema(example = "1")
        int quantity
) {
}
