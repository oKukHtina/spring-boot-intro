package com.example.service.impl;

import com.example.dto.request.CategoryRequestDto;
import com.example.dto.response.CategoryResponseDto;
import com.example.entity.Category;
import com.example.exception.CategoryNotFoundException;
import com.example.mapper.CategoryMapper;
import com.example.repository.CategoryRepository;
import com.example.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryResponseDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public CategoryResponseDto getById(Long id) {
        return categoryMapper.toDto(findCategoryById(id));
    }

    @Override
    public CategoryResponseDto save(CategoryRequestDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryResponseDto update(Long id, CategoryRequestDto categoryDto) {
        Category category = findCategoryById(id);
        categoryMapper.updateCategoryFromDto(categoryDto, category);

        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.delete(findCategoryById(id));
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category was not found by id : " + id
                ));
    }
}
