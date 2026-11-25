package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.config.CustomMySqlContainer;
import com.example.entity.Book;
import com.example.entity.Category;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class BookRepositoryTest {
    @Container
    private static CustomMySqlContainer mySqlContainer = CustomMySqlContainer.getInstance();
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySqlContainer::getUsername);
        registry.add("spring.datasource.password", mySqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", mySqlContainer::getDriverClassName);
    }

    @Test
    void findAllByCategoriesId_WithExistingCategoryId_ShouldReturnBooks() {
        Category category = new Category();
        category.setName("Fantasy");
        category.setDescription("Books about magical worlds");

        Category savedCategory = categoryRepository.save(category);

        Book book = new Book();
        book.setTitle("Neverland");
        book.setAuthor("John Ohn");
        book.setIsbn("168-1684967880");
        book.setPrice(new BigDecimal("60.00"));
        book.setCoverImage("https://example.com/book-cover-12.jpg");
        book.setDescription("A story about a magical world.");

        book.setCategories(Set.of(savedCategory));

        bookRepository.save(book);
        Page<Book> result = bookRepository.findAllByCategoriesId(
                savedCategory.getId(),
                PageRequest.of(0, 10)
        );

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertFalse(result.isEmpty());
        assertEquals("Neverland", result.getContent().get(0).getTitle());
    }

    @Test
    void findAllByCategoriesId_WithCategoryWithoutBooks_ShouldReturnEmptyPage() {
        Category category = new Category();
        category.setName("Horror");
        category.setDescription("Scary stories");

        Category savedCategory = categoryRepository.save(category);

        Page<Book> result = bookRepository.findAllByCategoriesId(
                savedCategory.getId(),
                PageRequest.of(0, 10)
        );

        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
    }

}
