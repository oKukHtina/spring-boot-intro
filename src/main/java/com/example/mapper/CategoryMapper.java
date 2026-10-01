package com.example.mapper;

import com.example.dto.request.CategoryRequestDto;
import com.example.dto.response.CategoryResponseDto;
import com.example.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    Category toEntity(CategoryRequestDto categoryRequestDto);

    void updateCategoryFromDto(
            CategoryRequestDto categoryRequestDto,
            @MappingTarget Category category
    );
}
