package com.example.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class BookResponseDto {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "NeverLand1")
    private String title;
    @Schema(example = "John Ohn")
    private String author;
    @Schema(example = "168-1684967880")
    private String isbn;
    @Schema(example = "60.00")
    private BigDecimal price;
    @Schema(example = "https://example.com/book-cover-12.jpg")
    private String coverImage;
    @Schema(example = "A story about a magical world.")
    private String description;
    @ArraySchema(schema = @Schema(example = "3"))
    private List<Long> categoryIds;
}
