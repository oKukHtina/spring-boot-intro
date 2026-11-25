package com.example.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.dto.request.CreateBookRequestDto;
import com.example.dto.response.BookResponseDto;
import com.example.entity.Book;
import com.example.entity.Category;
import com.example.mapper.BookMapper;
import com.example.repository.BookRepository;
import com.example.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Save book with valid request and existing categories")
    void save_WithValidBookRequestDto_ShouldReturnBookResponseDto() {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle("NeverLand1");
        bookRequestDto.setAuthor("John Ohn");
        bookRequestDto.setIsbn("168-1684967880");
        bookRequestDto.setPrice(new BigDecimal("60.00"));
        bookRequestDto.setCoverImage("https://example.com/book-cover-12.jpg");
        bookRequestDto.setDescription("A story about a magical world.");
        bookRequestDto.setCategoryIds(List.of(1L));

        Category category = new Category();
        category.setId(1L);

        Book book = new Book();
        book.setTitle("NeverLand1");
        book.setAuthor("John Ohn");
        book.setIsbn("168-1684967880");
        book.setPrice(new BigDecimal("60.00"));

        BookResponseDto expectedResponse = new BookResponseDto();
        expectedResponse.setId(1L);
        expectedResponse.setTitle("NeverLand1");

        when(bookMapper.toModel(bookRequestDto)).thenReturn(book);
        when(categoryRepository.findAllById(List.of(1L)))
                .thenReturn(List.of(category));
        when(bookMapper.toDto(book)).thenReturn(expectedResponse);

        BookResponseDto actualResponse = bookService.save(bookRequestDto);

        assertSame(expectedResponse, actualResponse);
        assertEquals(
                Set.of(category),
                book.getCategories()
        );

        verify(bookRepository).save(book);
        verify(bookMapper).toModel(bookRequestDto);
        verify(categoryRepository).findAllById(List.of(1L));
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Save book with non existing category")
    void save_WithNonExistingCategory_ShouldThrowEntityNotFoundException() {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setCategoryIds(List.of(1L, 2L));

        Book book = new Book();
        when(bookMapper.toModel(bookRequestDto)).thenReturn(book);

        Category category = new Category();
        category.setId(1L);

        when(
                categoryRepository.findAllById(List.of(1L, 2L)))
                .thenReturn(List.of(category)
                );

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> bookService.save(bookRequestDto)
                );
        assertEquals(
                "One or more categories were not found",
                exception.getMessage()
        );

        verify(bookRepository, never()).save(any(Book.class));
        verify(bookMapper, never()).toDto(any(Book.class));

    }

    @Test
    @DisplayName("Get book with existing id")
    void getBookById_WithExistingId_ShouldReturnBookResponseDto() {
        Long bookId = 10L;

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("NeverLand1");
        book.setAuthor("John Ohn");

        BookResponseDto expectedResponse = new BookResponseDto();
        expectedResponse.setId(bookId);
        expectedResponse.setTitle("NeverLand1");
        expectedResponse.setAuthor("John Ohn");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectedResponse);

        BookResponseDto actualResponse = bookService.getBookById(bookId);

        assertSame(expectedResponse, actualResponse);
        verify(bookRepository).findById(bookId);
        verify(bookMapper).toDto(book);

        assertEquals(bookId, actualResponse.getId());
        assertEquals("NeverLand1", actualResponse.getTitle());
    }

    @Test
    @DisplayName("Get book with non existing id")
    void getBookById_WithNonExistingId_ShouldThrowEntityNotFoundException() {
        Long bookId = 10L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.getBookById(bookId));

        assertEquals(
                "Book was not found by id : " + bookId,
                exception.getMessage()
        );

        verify(bookRepository).findById(bookId);
        verify(bookMapper, never()).toDto(any(Book.class));
    }

    @Test
    @DisplayName("Get all books")
    void findAll_ShouldReturnPageOfBookResponseDto() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("NeverLand1");

        BookResponseDto expectedResponse = new BookResponseDto();
        expectedResponse.setId(1L);
        expectedResponse.setTitle("NeverLand1");

        Page<Book> bookPage = new PageImpl<>(List.of(book));
        Pageable pageable = PageRequest.of(0, 10);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(expectedResponse);

        Page<BookResponseDto> actualResponse = bookService.findAll(pageable);

        assertEquals(1, actualResponse.getTotalElements());
        assertEquals(expectedResponse, actualResponse.getContent().get(0));

        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Update book with valid request and existing categories")
    void updateBook_WithValidId_ShouldReturnBookResponseDto() {
        Long bookId = 1L;

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("NeverLand1");
        book.setAuthor("John Ohn");

        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle("NeverLand2");
        bookRequestDto.setAuthor("New Author");
        bookRequestDto.setIsbn("168-1684967880");
        bookRequestDto.setPrice(new BigDecimal("75.00"));
        bookRequestDto.setDescription("A new description");
        bookRequestDto.setCategoryIds(List.of(2L));

        Category category = new Category();
        category.setId(2L);

        BookResponseDto expectedResponse = new BookResponseDto();
        expectedResponse.setId(bookId);
        expectedResponse.setTitle("NeverLand2");
        expectedResponse.setAuthor("New Author");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(categoryRepository.findAllById(List.of(2L))).thenReturn(List.of(category));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expectedResponse);

        BookResponseDto actualResponse = bookService.updateBook(bookId, bookRequestDto);

        assertSame(expectedResponse, actualResponse);
        assertEquals(Set.of(category), book.getCategories());

        verify(bookRepository).findById(bookId);
        verify(categoryRepository).findAllById(List.of(2L));
        verify(bookMapper).updateBookFromDto(bookRequestDto, book);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Delete book with existing id")
    void deleteById_WithExistingId_ShouldDeleteBook() {
        Long bookId = 2L;

        when(bookRepository.existsById(bookId)).thenReturn(true);
        bookService.deleteById(bookId);

        verify(bookRepository).existsById(bookId);
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    @DisplayName("Delete book with non existing id")
    void deleteById_WithNonExistingId_ShouldThrowEntityNotFoundException() {
        Long bookId = 2L;

        when(bookRepository.existsById(bookId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.deleteById(bookId)
        );

        assertEquals(
                "Book was not found by id : " + bookId,
                exception.getMessage()
        );

        verify(bookRepository).existsById(bookId);
        verify(bookRepository, never()).deleteById(bookId);
    }
}
