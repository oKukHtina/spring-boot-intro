package com.example.mapper;

import com.example.config.dto.request.CategoryRequestDto;
import com.example.config.dto.response.CategoryResponseDto;
import com.example.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Category toEntity(CategoryRequestDto categoryRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateCategoryFromDto(
            CategoryRequestDto categoryRequestDto,
            @MappingTarget Category category
    );
}
