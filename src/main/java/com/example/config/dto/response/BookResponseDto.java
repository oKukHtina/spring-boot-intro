package com.example.config.dto.response;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class BookResponseDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String coverImage;
    private String description;
    private List<Long> categoryIds;
}
