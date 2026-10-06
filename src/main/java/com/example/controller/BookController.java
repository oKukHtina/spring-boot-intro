package com.example.controller;

import com.example.config.dto.request.BookSearchParametersDto;
import com.example.config.dto.request.CreateBookRequestDto;
import com.example.config.dto.response.BookResponseDto;
import com.example.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Book management", description = "Endpoints for managing books")
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping
    @Operation(summary = "Get all books", description = "Get page of all available books")
    public Page<BookResponseDto> getAll(
            @PageableDefault(size = 10, sort = "title") Pageable pageable
    ) {
        return bookService.findAll(pageable);
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Book by Id", description = "Return book by Id")
    public BookResponseDto getBookById(
            @PathVariable Long id
    ) {
        return bookService.getBookById(id);
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping("/search")
    @Operation(summary = "Search book", description = "Get a list of books by specification")
    public Page<BookResponseDto> searchBooks(
            BookSearchParametersDto searchParameters,
            @PageableDefault(size = 10, sort = "title") Pageable pageable
    ) {
        return bookService.search(searchParameters, pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a book", description = "Create a new book")
    public BookResponseDto createBook(
            @RequestBody @Valid CreateBookRequestDto bookDto
    ) {
        return bookService.save(bookDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update book", description = "Update book by Id")
    public BookResponseDto updateBook(
            @PathVariable Long id,
            @RequestBody @Valid CreateBookRequestDto bookDto
    ) {
        return bookService.updateBook(id, bookDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book", description = "Delete a book by Id")
    public void deleteBookById(
            @PathVariable Long id
    ) {
        bookService.deleteById(id);
    }
}
