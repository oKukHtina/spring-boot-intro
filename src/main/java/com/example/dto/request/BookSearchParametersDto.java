package com.example.dto.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

public record BookSearchParametersDto(
        @ArraySchema(
                arraySchema = @Schema(description = "Filter books by authors"),
                schema = @Schema(example = "J.K. Rowling")
        )
        String[] authors,
        @ArraySchema(
                arraySchema = @Schema(description = "Filter books by titles"),
                schema = @Schema(example = "Harry Potter")
        )
        String[] titles
) {
}
