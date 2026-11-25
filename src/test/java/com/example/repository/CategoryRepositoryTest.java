package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.config.CustomMySqlContainer;
import com.example.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class CategoryRepositoryTest {
    @Container
    private static CustomMySqlContainer mySqlContainer = CustomMySqlContainer.getInstance();
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
    public void save_WithValidCategory_ShouldSaveCategory() {
        Category category = new Category();
        category.setName("Fantasy");
        category.setDescription("Books about magical worlds");

        Category savedCategory = categoryRepository.save(category);

        assertNotNull(savedCategory.getId());
        assertEquals("Fantasy", savedCategory.getName());
        assertEquals("Books about magical worlds", savedCategory.getDescription());
    }
}
