package com.example.config.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDto(
        @NotBlank(message = "Name cannot be blank")
        String name,
        String description
) {
}
