package com.example.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OrderRequestDto(
        @Schema(example = "15 Shevchenko Street, Kyiv")
        @NotBlank
        @Size(max = 255)
        String shippingAddress
) {
}
