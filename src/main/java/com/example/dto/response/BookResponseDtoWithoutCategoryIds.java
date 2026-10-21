package com.example.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record BookResponseDtoWithoutCategoryIds(
        @Schema(example = "1")
        Long id,
        @Schema(example = "NeverLand1")
        String title,
        @Schema(example = "John Ohn")
        String author,
        @Schema(example = "168-1684967880")
        String isbn,
        @Schema(example = "60.00")
        BigDecimal price,
        @Schema(example = "https://example.com/book-cover.jpg")
        String coverImage,
        @Schema(example = "A story about a magical world.")
        String description
) {
}
