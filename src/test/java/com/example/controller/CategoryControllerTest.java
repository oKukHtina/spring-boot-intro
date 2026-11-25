package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.config.CustomMySqlContainer;
import com.example.dto.request.CategoryRequestDto;
import com.example.entity.Book;
import com.example.entity.Category;
import com.example.repository.BookRepository;
import com.example.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
class CategoryControllerTest {
    @Container
    private static CustomMySqlContainer mySqlContainer =
            CustomMySqlContainer.getInstance();
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySqlContainer::getUsername);
        registry.add("spring.datasource.password", mySqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name",
                mySqlContainer::getDriverClassName);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Create category should return the created category with HTTP 201")
    void createCategory_ShouldReturnCreatedCategory() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto(
                "Fantasy",
                "Books about magical worlds"
        );

        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Fantasy"))
                .andExpect(jsonPath("$.description")
                        .value("Books about magical worlds"));

        List<Category> categories = categoryRepository.findAll();

        assertTrue(categories.stream()
                .anyMatch(category -> category.getName().equals("Fantasy")));
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Get all categories should return a page of categories")
    void getAllCategories_ShouldReturnPageOfCategories() throws Exception {
        Category firstCategory = new Category();
        firstCategory.setName("Fantasy");
        firstCategory.setDescription("Magical worlds");

        Category secondCategory = new Category();
        secondCategory.setName("Science Fiction");
        secondCategory.setDescription("Futuristic worlds");

        categoryRepository.saveAll(List.of(firstCategory, secondCategory));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[*].name",
                        org.hamcrest.Matchers.containsInAnyOrder(
                                "Fantasy", "Science Fiction"
                        )));
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Get category by ID should return the requested category")
    void getCategoryById_ShouldReturnCategory() throws Exception {
        Category category = new Category();
        category.setName("Fantasy");
        category.setDescription("Books about magical worlds");

        Category savedCategory = categoryRepository.save(category);

        mockMvc.perform(get("/categories/{id}", savedCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedCategory.getId()))
                .andExpect(jsonPath("$.name").value("Fantasy"))
                .andExpect(jsonPath("$.description").value("Books about magical worlds"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Update category should return the updated category")
    void updateCategory_ShouldReturnUpdatedCategory() throws Exception {
        Category category = new Category();
        category.setName("Science Fiction");
        category.setDescription("Books about futuristic worlds");

        Category savedCategory = categoryRepository.save(category);

        CategoryRequestDto requestDto = new CategoryRequestDto(
                "Fantasy",
                "Books about magical worlds"
        );

        mockMvc.perform(put("/categories/{id}", savedCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedCategory.getId()))
                .andExpect(jsonPath("$.name").value("Fantasy"))
                .andExpect(jsonPath("$.description")
                        .value("Books about magical worlds"));

        Category updatedCategory = categoryRepository.findById(savedCategory.getId())
                .orElseThrow();

        Assertions.assertEquals(
                "Fantasy", updatedCategory.getName()
        );
        Assertions.assertEquals(
                "Books about magical worlds", updatedCategory.getDescription()
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Delete category should return HTTP 204")
    void deleteCategory_ShouldReturnNoContent() throws Exception {
        Category category = new Category();
        category.setName("Fantasy");
        category.setDescription("Books about magical worlds");

        Category savedCategory = categoryRepository.save(category);

        mockMvc.perform(delete("/categories/{id}", savedCategory.getId()))
                .andExpect(status().isNoContent());

        Assertions.assertTrue(
                categoryRepository.findById(savedCategory.getId()).isEmpty()
        );
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Find books by category should return a page of books")
    void findBooksByCategory_ShouldReturnBooks() throws Exception {
        Category category = new Category();
        category.setName("Fantasy");
        category.setDescription("Books about magical worlds");

        Category savedCategory = categoryRepository.save(category);

        Book firstBook = new Book();
        firstBook.setTitle("Neverland");
        firstBook.setAuthor("John Ohn");
        firstBook.setIsbn("168-1684967880");
        firstBook.setPrice(new BigDecimal("60.00"));
        firstBook.setCategories(Set.of(savedCategory));

        Book secondBook = new Book();
        secondBook.setTitle("Magic World");
        secondBook.setAuthor("Jane Doe");
        secondBook.setIsbn("978-1234567890");
        secondBook.setPrice(new BigDecimal("45.00"));
        secondBook.setCategories(Set.of(savedCategory));

        bookRepository.saveAll(List.of(firstBook, secondBook));

        mockMvc.perform(get("/categories/{id}/books", savedCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[*].title",
                        org.hamcrest.Matchers.containsInAnyOrder(
                                "Neverland", "Magic World"
                        )));
    }
}
