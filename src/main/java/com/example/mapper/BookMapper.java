package com.example.mapper;

import com.example.dto.request.CreateBookRequestDto;
import com.example.dto.response.BookResponseDto;
import com.example.dto.response.BookResponseDtoWithoutCategoryIds;
import com.example.entity.Book;
import com.example.entity.Category;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookResponseDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Book toModel(CreateBookRequestDto bookRequestDto);

    BookResponseDtoWithoutCategoryIds toDtoWithoutCategoryIds(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "categories", ignore = true)
    void updateBookFromDto(CreateBookRequestDto dto, @MappingTarget Book book);

    @AfterMapping
    default void setCategoryIds(
            @MappingTarget BookResponseDto bookDto,
            Book book
    ) {
        List<Long> categoryIds = book.getCategories()
                .stream()
                .map(Category::getId)
                .toList();

        bookDto.setCategoryIds(categoryIds);
    }
}
