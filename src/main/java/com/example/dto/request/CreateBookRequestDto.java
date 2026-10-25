package com.example.dto.request;

import com.example.validation.annotation.Isbn;
import com.example.validation.annotation.Price;
import com.example.validation.annotation.Title;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @Schema(example = "NeverLand1")
    @NotBlank(message = "Title cannot be blank")
    @Title
    private String title;

    @Schema(example = "John Ohn")
    @NotBlank(message = "Author cannot be blank")
    private String author;

    @Schema(example = "168-1684967880")
    @NotBlank(message = "Isbn cannot be blank")
    @Isbn
    private String isbn;

    @Schema(example = "60.00")
    @NotNull(message = "Price cannot be null")
    @Price
    private BigDecimal price;

    @Schema(example = "https://example.com/book-cover-12.jpg")
    @NotBlank(message = "CoverImage cannot be blank")
    private String coverImage;

    @Schema(example = "A story about a magical world.")
    @NotBlank(message = "Description cannot be blank")
    private String description;

    @ArraySchema(
            schema = @Schema(example = "3"),
            arraySchema = @Schema(description = "IDs of book categories")
    )
    @NotEmpty(message = "Categories cannot be empty")
    private List<Long> categoryIds;
}
