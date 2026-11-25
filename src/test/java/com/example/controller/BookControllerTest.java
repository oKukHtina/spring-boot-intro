package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.config.CustomMySqlContainer;
import com.example.dto.request.CreateBookRequestDto;
import com.example.entity.Book;
import com.example.entity.Category;
import com.example.repository.BookRepository;
import com.example.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
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
class BookControllerTest {
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
    @WithMockUser(authorities = "USER")
    @DisplayName("Get all books should return a page of books")
    void getAll_ShouldReturnPageOfBooks() throws Exception {
        Book firstBook = new Book();
        firstBook.setTitle("Neverland");
        firstBook.setAuthor("John Ohn");
        firstBook.setIsbn("168-1684967880");
        firstBook.setPrice(new BigDecimal("60.00"));

        Book secondBook = new Book();
        secondBook.setTitle("Harry Potter");
        secondBook.setAuthor("J. K. Rowling");
        secondBook.setIsbn("222-2222222222");
        secondBook.setPrice(new BigDecimal("75.00"));

        bookRepository.saveAll(List.of(firstBook, secondBook));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].title").value("Harry Potter"))
                .andExpect(jsonPath("$.content[1].title").value("Neverland"));
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Get book by ID should return the requested book")
    void getBookById_ShouldReturnBook() throws Exception {
        Book book = new Book();
        book.setTitle("Neverland");
        book.setAuthor("John Ohn");
        book.setIsbn("168-1684967880");
        book.setPrice(new BigDecimal("60.00"));
        book.setCoverImage("https://example.com/book-cover.jpg");
        book.setDescription("A story about a magical world.");

        Book savedBook = bookRepository.save(book);

        mockMvc.perform(get("/books/" + savedBook.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedBook.getId()))
                .andExpect(jsonPath("$.title").value("Neverland"))
                .andExpect(jsonPath("$.author").value("John Ohn"))
                .andExpect(jsonPath("$.isbn").value("168-1684967880"))
                .andExpect(jsonPath("$.price").value(60.00))
                .andExpect(jsonPath("$.coverImage")
                        .value("https://example.com/book-cover.jpg"))
                .andExpect(jsonPath("$.description")
                        .value("A story about a magical world."));
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Search books by author should return matching books")
    void searchBooksByAuthor_ShouldReturnMatchingBooks() throws Exception {
        Book firstBook = new Book();
        firstBook.setTitle("Harry Potter");
        firstBook.setAuthor("J.K. Rowling");
        firstBook.setIsbn("168-1684967880");
        firstBook.setPrice(new BigDecimal("60.00"));

        Book secondBook = new Book();
        secondBook.setTitle("Neverland");
        secondBook.setAuthor("John Ohn");
        secondBook.setIsbn("168-1684967881");
        secondBook.setPrice(new BigDecimal("75.00"));

        bookRepository.saveAll(List.of(firstBook, secondBook));

        mockMvc.perform(get("/books/search")
                        .param("authors", "J.K. Rowling"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Harry Potter"))
                .andExpect(jsonPath("$.content[0].author").value("J.K. Rowling"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Create book should return the created book with HTTP 201")
    void createBook_ShouldReturnCreatedBook() throws Exception {
        Category category = new Category();
        category.setName("Fantasy");
        category.setDescription("Fantasy books");

        Category savedCategory = categoryRepository.save(category);

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("NeverLand1");
        requestDto.setAuthor("John Ohn");
        requestDto.setIsbn("168-1684967880");
        requestDto.setPrice(new BigDecimal("60.00"));
        requestDto.setCoverImage("https://example.com/book-cover.jpg");
        requestDto.setDescription("A story about a magical world.");
        requestDto.setCategoryIds(List.of(savedCategory.getId()));

        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("NeverLand1"))
                .andExpect(jsonPath("$.author").value("John Ohn"))
                .andExpect(jsonPath("$.isbn").value("168-1684967880"))
                .andExpect(jsonPath("$.price").value(60.00))
                .andExpect(jsonPath("$.categoryIds[0]")
                        .value(savedCategory.getId()));

        List<Book> books = bookRepository.findAll();

        assertEquals(1, books.size());
        assertEquals("NeverLand1", books.get(0).getTitle());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Update book should return the updated book")
    void updateBook_ShouldReturnUpdatedBook() throws Exception {
        Category category = new Category();
        category.setName("Fantasy");
        category.setDescription("Fantasy books");

        Category savedCategory = categoryRepository.save(category);

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("NeverLand2");
        requestDto.setAuthor("John Ohn");
        requestDto.setIsbn("168-1684967880");
        requestDto.setPrice(new BigDecimal("70.00"));
        requestDto.setDescription("A story about a magical world.");
        requestDto.setCoverImage("https://example.com/new-cover.jpg");
        requestDto.setCategoryIds(List.of(savedCategory.getId()));

        Book book = new Book();
        book.setTitle("NeverLand1");
        book.setAuthor("John Ohhn");
        book.setIsbn("168-1684967880");
        book.setPrice(new BigDecimal("40.00"));
        book.setDescription("A story about a magical world.");

        Book savedBook = bookRepository.save(book);

        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/books/{id}", savedBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedBook.getId()))
                .andExpect(jsonPath("$.title").value("NeverLand2"))
                .andExpect(jsonPath("$.author").value("John Ohn"))
                .andExpect(jsonPath("$.isbn").value("168-1684967880"))
                .andExpect(jsonPath("$.price").value(70.00));

        Book updatedBook = bookRepository.findById(savedBook.getId())
                .orElseThrow();

        assertEquals("NeverLand2", updatedBook.getTitle());
        assertEquals("John Ohn", updatedBook.getAuthor());
        assertEquals("168-1684967880", updatedBook.getIsbn());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Delete book should return HTTP 204")
    void deleteBookById_ShouldReturnNoContent() throws Exception {
        Book book = new Book();
        book.setTitle("Book to delete");
        book.setAuthor("John Ohn");
        book.setIsbn("168-1684967880");
        book.setPrice(new BigDecimal("50.00"));

        Book savedBook = bookRepository.save(book);

        mockMvc.perform(delete("/books/{id}", savedBook.getId()))
                .andExpect(status().isNoContent());

        assertTrue(bookRepository.findById(savedBook.getId()).isEmpty());
    }
}
