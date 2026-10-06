package com.example.service;

import com.example.config.dto.request.BookSearchParametersDto;
import com.example.config.dto.request.CreateBookRequestDto;
import com.example.config.dto.response.BookResponseDto;
import com.example.config.dto.response.BookResponseDtoWithoutCategoryIds;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto save(CreateBookRequestDto bookRequestDto);

    Page<BookResponseDto> findAll(Pageable pageable);

    BookResponseDto getBookById(Long id);

    void deleteById(Long id);

    BookResponseDto updateBook(Long id, CreateBookRequestDto bookDto);

    Page<BookResponseDto> search(BookSearchParametersDto searchParameters, Pageable pageable);

    Page<BookResponseDtoWithoutCategoryIds> findBooksByCategoryId(Long id, Pageable pageable);
}
