package com.example.dto;

import com.example.validation.annotation.Isbn;
import com.example.validation.annotation.Price;
import com.example.validation.annotation.Title;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank(message = "Title cannot be blank")
    @Title
    private String title;

    @NotBlank(message = "Author cannot be blank")
    private String author;

    @NotBlank(message = "Isbn cannot be blank")
    @Isbn
    private String isbn;

    @NotNull(message = "Price cannot be null")
    @Price
    private BigDecimal price;

    @NotBlank(message = "CoverImage cannot be blank")
    private String coverImage;

    @NotBlank(message = "Description cannot be blank")
    private String description;
}
