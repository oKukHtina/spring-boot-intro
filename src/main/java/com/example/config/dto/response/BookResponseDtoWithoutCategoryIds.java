package com.example.config.dto.response;

import java.math.BigDecimal;

public record BookResponseDtoWithoutCategoryIds(
         Long id,
         String title,
         String author,
         String isbn,
         BigDecimal price,
         String coverImage,
         String description
) {
}
