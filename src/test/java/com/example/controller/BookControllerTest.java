package com.example.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
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

import com.example.dto.request.BookSearchParametersDto;
import com.example.dto.request.CreateBookRequestDto;
import com.example.dto.response.BookResponseDto;
import com.example.security.JwtUtil;
import com.example.service.BookService;
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

@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private BookService bookService;

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Get all books should return a page of books")
    void getAll_ShouldReturnPageOfBooks() throws Exception {
        BookResponseDto firstBook = new BookResponseDto();
        firstBook.setId(1L);
        firstBook.setTitle("Neverland");
        firstBook.setAuthor("John Ohn");

        BookResponseDto secondBook = new BookResponseDto();
        secondBook.setId(2L);
        secondBook.setTitle("Harry Potter");
        secondBook.setAuthor("J. K. Rowling");

        Pageable pageable = PageRequest.of(0, 10);

        Page<BookResponseDto> booksPage = new PageImpl<>(
                List.of(firstBook, secondBook),
                pageable,
                2
        );

        when(bookService.findAll(any(Pageable.class)))
                .thenReturn(booksPage);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].title").value("Neverland"))
                .andExpect(jsonPath("$.content[1].title").value("Harry Potter"));
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Get book by ID should return the requested book")
    void getBookById_ShouldReturnBook() throws Exception {
        Long bookId = 1L;

        BookResponseDto book = new BookResponseDto();
        book.setId(bookId);
        book.setTitle("Neverland");
        book.setAuthor("John Ohn");
        book.setIsbn("168-1684967880");
        book.setPrice(new BigDecimal("60.00"));
        book.setCoverImage("https://example.com/book-cover.jpg");
        book.setDescription("A story about a magical world.");
        book.setCategoryIds(List.of(1L));

        when(bookService.getBookById(bookId))
                .thenReturn(book);

        mockMvc.perform(get("/books/" + bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("Neverland"))
                .andExpect(jsonPath("$.author").value("John Ohn"));

    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Search books by author should return matching books")
    void searchBooksByAuthor_ShouldReturnMatchingBooks() throws Exception {
        BookResponseDto book = new BookResponseDto();
        book.setId(1L);
        book.setTitle("Harry Potter");
        book.setAuthor("J.K. Rowling");

        Pageable pageable = PageRequest.of(0, 10);

        Page<BookResponseDto> booksPage = new PageImpl<>(
                List.of(book),
                pageable,
                1
        );

        when(bookService.search(any(BookSearchParametersDto.class),
                any(Pageable.class))).thenReturn(booksPage);
        mockMvc.perform(get("/books/search")
                        .param("authors", "J.K. Rowling"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Harry Potter"))
                .andExpect(jsonPath("$.content[0].author").value("J.K. Rowling"));

        verify(bookService).search(
                argThat(params ->
                        params != null
                                && params.authors() != null
                                && List.of(params.authors()).contains("J.K. Rowling")
                                && params.titles() == null
                ),
                any(Pageable.class)
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Create book should return the created book with HTTP 201")
    void createBook_ShouldReturnCreatedBook() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("NeverLand1");
        requestDto.setAuthor("John Ohn");
        requestDto.setIsbn("168-1684967880");
        requestDto.setPrice(new BigDecimal("60.00"));
        requestDto.setCoverImage("https://example.com/book-cover.jpg");
        requestDto.setDescription("A story about a magical world.");
        requestDto.setCategoryIds(List.of(1L));

        BookResponseDto expectedBook = new BookResponseDto();
        expectedBook.setId(1L);
        expectedBook.setTitle("NeverLand1");
        expectedBook.setAuthor("John Ohn");
        expectedBook.setIsbn("168-1684967880");
        expectedBook.setPrice(new BigDecimal("60.00"));
        expectedBook.setCoverImage("https://example.com/book-cover.jpg");
        expectedBook.setDescription("A story about a magical world.");
        expectedBook.setCategoryIds(List.of(1L));

        when(bookService.save(any(CreateBookRequestDto.class))).thenReturn(expectedBook);

        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("NeverLand1"))
                .andExpect(jsonPath("$.author").value("John Ohn"));

    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Update book should return the updated book")
    void updateBook_ShouldReturnUpdatedBook() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Updated Book");
        requestDto.setAuthor("John Ohn");
        requestDto.setIsbn("168-1684967880");
        requestDto.setPrice(new BigDecimal("75.00"));
        requestDto.setCoverImage("https://example.com/book-cover.jpg");
        requestDto.setDescription("Updated description");
        requestDto.setCategoryIds(List.of(1L));

        Long bookId = 1L;

        BookResponseDto expectedBook = new BookResponseDto();
        expectedBook.setId(bookId);
        expectedBook.setTitle("Updated Book");
        expectedBook.setAuthor("John Ohn");
        expectedBook.setIsbn("168-1684967880");
        expectedBook.setPrice(new BigDecimal("75.00"));
        expectedBook.setCoverImage("https://example.com/book-cover.jpg");
        expectedBook.setDescription("Updated description");
        expectedBook.setCategoryIds(List.of(1L));

        when(
                bookService.updateBook(
                eq(bookId),
                any(CreateBookRequestDto.class)
        )
        ).thenReturn(expectedBook);

        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/books/{id}", bookId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Book"))
                .andExpect(jsonPath("$.author").value("John Ohn"));

    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Delete category should return HTTP 204")
    void deleteBookById_ShouldReturnNoContent() throws Exception {
        Long bookId = 1L;

        mockMvc.perform(delete("/books/{id}", bookId)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(bookService).deleteById(bookId);
    }
}
