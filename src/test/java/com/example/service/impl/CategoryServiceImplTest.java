package com.example.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.dto.request.CategoryRequestDto;
import com.example.dto.response.CategoryResponseDto;
import com.example.entity.Category;
import com.example.mapper.CategoryMapper;
import com.example.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void findAll_ShouldReturnPageOfCategoryResponseDto() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Fantasy");
        category.setDescription("Books about magical worlds and adventures");

        CategoryResponseDto expectedResponse =
                new CategoryResponseDto(1L, "Fantasy", "Books about magical worlds and adventures");

        Page<Category> categoryPage = new PageImpl<>(List.of(category));
        Pageable pageable = PageRequest.of(0, 10);

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(expectedResponse);

        Page<CategoryResponseDto> actualResponse = categoryService.findAll(pageable);

        assertEquals(1, actualResponse.getTotalElements());
        assertEquals(expectedResponse, actualResponse.getContent().get(0));

        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Get category by existing id")
    void getById_WithExistingId_ShouldReturnCategoryResponseDto() {
        Long categoryId = 3L;

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Fantasy");
        category.setDescription("Books about magical worlds");

        CategoryResponseDto expectedResponse = new CategoryResponseDto(
                categoryId,
                "Fantasy",
                "Books about magical worlds"
        );

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expectedResponse);

        CategoryResponseDto actualResponse = categoryService.getById(categoryId);

        assertSame(expectedResponse, actualResponse);

        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Get category by non existing id")
    void getById_WithNonExistingId_ShouldThrowEntityNotFoundException() {
        Long categoryId = 2L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> categoryService.getById(categoryId)
        );

        assertEquals(
                "Category was not found by id : " + categoryId,
                exception.getMessage()
        );

        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper, never()).toDto(any(Category.class));
    }

    @Test
    @DisplayName("Save category with valid request")
    void save_WithValidCategoryRequestDto_ShouldReturnCategoryResponseDto() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Fantasy");
        category.setDescription("Books about magical worlds");

        CategoryRequestDto categoryRequestDto =
                new CategoryRequestDto(
                        "Fantasy",
                        "Books about magical worlds"
                );

        when(categoryMapper.toEntity(categoryRequestDto)).thenReturn(category);

        CategoryResponseDto expectedResponseDto =
                new CategoryResponseDto(
                        1L,
                        "Fantasy",
                        "Books about magical worlds");

        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expectedResponseDto);

        CategoryResponseDto actualResponse = categoryService.save(categoryRequestDto);

        assertSame(expectedResponseDto, actualResponse);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toEntity(categoryRequestDto);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Update category with valid request")
    void update_WithValidId_ShouldReturnCategoryResponseDto() {
        Long categoryId = 2L;

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Fantasy");
        category.setDescription("Books about magical worlds");

        CategoryRequestDto categoryRequestDto =
                new CategoryRequestDto("Science Fiction", "About smth");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        CategoryResponseDto expectedResponse =
                new CategoryResponseDto(categoryId, "Science Fiction", "About smth");

        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expectedResponse);

        CategoryResponseDto actualResponse = categoryService.update(categoryId, categoryRequestDto);

        assertSame(expectedResponse, actualResponse);
        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).updateCategoryFromDto(categoryRequestDto, category);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Delete category with existing id")
    void deleteById_WithExistingId_ShouldDeleteCategory() {
        Long categoryId = 2L;

        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        categoryService.deleteById(categoryId);

        verify(categoryRepository).existsById(categoryId);
        verify(categoryRepository).deleteById(categoryId);
    }

    @Test
    @DisplayName("Delete category with non existing id")
    void deleteById_WithNonExistingId_ShouldThrowEntityNotFoundException() {
        Long categoryId = 2L;

        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.deleteById(categoryId)
        );

        assertEquals(
                "Category was not found by id : " + categoryId,
                exception.getMessage()
        );

        verify(categoryRepository).existsById(categoryId);
        verify(categoryRepository, never()).deleteById(categoryId);
    }
}
