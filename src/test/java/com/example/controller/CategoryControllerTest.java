package com.example.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.dto.request.CategoryRequestDto;
import com.example.dto.response.BookResponseDtoWithoutCategoryIds;
import com.example.dto.response.CategoryResponseDto;
import com.example.security.JwtUtil;
import com.example.service.BookService;
import com.example.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private BookService bookService;

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Create category should return the created category with HTTP 201")
    void createCategory_ShouldReturnCreatedCategory() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto(
                "Fantasy",
                "Books about magical worlds"
        );

        CategoryResponseDto expectedCategory = new CategoryResponseDto(
                1L,
                "Fantasy",
                "Books about magical worlds"
        );

        when(categoryService.save(any(CategoryRequestDto.class)))
                .thenReturn(expectedCategory);

        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/categories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Fantasy"))
                .andExpect(jsonPath("$.description").value("Books about magical worlds"));
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Get all categories should return a page of categories")
    void getAllCategories_ShouldReturnPageOfCategories() throws Exception {
        CategoryResponseDto firstCategory =
                new CategoryResponseDto(1L, "Fantasy", "Magical worlds");

        CategoryResponseDto secondCategory =
                new CategoryResponseDto(2L, "Science Fiction", "Futuristic worlds");

        Pageable pageable = PageRequest.of(0, 10);

        Page<CategoryResponseDto> categoriesPage = new PageImpl<>(
                List.of(firstCategory, secondCategory),
                pageable,
                2
        );

        when(categoryService.findAll(any(Pageable.class)))
                .thenReturn(categoriesPage);

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Fantasy"))
                .andExpect(jsonPath("$.content[1].name").value("Science Fiction"));
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Get category by ID should return the requested category")
    void getCategoryById_ShouldReturnCategory() throws Exception {
        Long categoryId = 1L;

        CategoryResponseDto category = new CategoryResponseDto(
                categoryId, "Fantasy", "Books about magical worlds"
        );

        when(categoryService.getById(categoryId))
                .thenReturn(category);

        mockMvc.perform(get("/categories/{id}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.name").value("Fantasy"))
                .andExpect(jsonPath("$.description").value("Books about magical worlds"));

    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Update category should return the updated category")
    void updateCategory_ShouldReturnUpdatedCategory() throws Exception {
        Long categoryId = 1L;

        CategoryRequestDto requestDto = new CategoryRequestDto(
                "Fantasy",
                "Books about magical worlds"
        );

        CategoryResponseDto updatedCategory = new CategoryResponseDto(
                categoryId,
                "Fantasy",
                "Books about magical worlds"
        );

        when(categoryService.update(
                eq(categoryId),
                any(CategoryRequestDto.class)
        )).thenReturn(updatedCategory);

        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/categories/{id}", categoryId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.name").value("Fantasy"))
                .andExpect(jsonPath("$.description")
                        .value("Books about magical worlds"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Delete category should return HTTP 204")
    void deleteCategory_ShouldReturnNoContent() throws Exception {
        Long categoryId = 1L;

        mockMvc.perform(delete("/categories/{id}", categoryId)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(categoryService).deleteById(categoryId);
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Find books by category should return a page of books")
    void findBooksByCategory_ShouldReturnBooks() throws Exception {
        Long categoryId = 1L;

        BookResponseDtoWithoutCategoryIds firstBook =
                new BookResponseDtoWithoutCategoryIds(
                        1L,
                        "NeverLand",
                        "John Ohn",
                        "168-1684967880",
                        new BigDecimal("60.00"),
                        "https://example.com/book-cover.jpg",
                        "A story about a magical world."
                );

        BookResponseDtoWithoutCategoryIds secondBook =
                new BookResponseDtoWithoutCategoryIds(
                        2L,
                        "Magic World",
                        "Jane Doe",
                        "978-1234567890",
                        new BigDecimal("45.00"),
                        "https://example.com/magic-cover.jpg",
                        "A book about magic."
                );

        Pageable pageable = PageRequest.of(0, 10);

        Page<BookResponseDtoWithoutCategoryIds> booksPage = new PageImpl<>(
                List.of(firstBook, secondBook),
                pageable,
                2
        );

        when(bookService.findBooksByCategoryId(
                eq(categoryId),
                any(Pageable.class)
        )).thenReturn(booksPage);

        mockMvc.perform(get("/categories/{id}/books", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].title").value("NeverLand"))
                .andExpect(jsonPath("$.content[0].author").value("John Ohn"))
                .andExpect(jsonPath("$.content[1].title").value("Magic World"))
                .andExpect(jsonPath("$.content[1].author").value("Jane Doe"));
    }
}
