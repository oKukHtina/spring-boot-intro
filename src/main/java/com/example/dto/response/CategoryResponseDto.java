package com.example.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryResponseDto(
        @Schema(example = "1")
        Long id,
        @Schema(example = "Fantasy")
        String name,
        @Schema(example = "Books about magical worlds and adventures")
        String description
) {
}
