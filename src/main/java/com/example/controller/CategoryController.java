package com.example.controller;

import com.example.dto.request.CategoryRequestDto;
import com.example.dto.response.BookResponseDtoWithoutCategoryIds;
import com.example.dto.response.CategoryResponseDto;
import com.example.service.BookService;
import com.example.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a category",
            description = "Create a new category"
    )
    public CategoryResponseDto createCategory(
            @RequestBody @Valid CategoryRequestDto categoryRequestDto
    ) {
        return categoryService.save(categoryRequestDto);
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping
    @Operation(
            summary = "Get all categories",
            description = "Get page of all available categories"
    )
    public Page<CategoryResponseDto> getAllCategories(
            @PageableDefault(size = 10, sort = "name") Pageable pageable
    ) {
        return categoryService.findAll(pageable);
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Get category", description = "Return category by Id")
    public CategoryResponseDto getCategoryById(
            @PathVariable Long id
    ) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Update category by Id")
    public CategoryResponseDto updateCategory(
            @PathVariable Long id,
            @RequestBody @Valid CategoryRequestDto categoryRequestDto
    ) {
        return categoryService.update(id, categoryRequestDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Delete specific category by Id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(
            @PathVariable Long id
    ) {
        categoryService.deleteById(id);
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping("/{id}/books")
    @Operation(
            summary = "Get books by category",
            description = "Retrieve books by a specific category"
    )
    public Page<BookResponseDtoWithoutCategoryIds> findBooksByCategory(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "title") Pageable pageable
    ) {
        return bookService.findBooksByCategoryId(id, pageable);
    }
}
