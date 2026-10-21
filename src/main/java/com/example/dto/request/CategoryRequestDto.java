package com.example.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDto(
        @Schema(example = "Fantasy")
        @NotBlank(message = "Name cannot be blank")
        String name,
        @Schema(example = "Books about magical worlds and adventures")
        String description
) {
}
