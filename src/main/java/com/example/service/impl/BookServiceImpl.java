package com.example.service.impl;

import com.example.dto.BookDto;
import com.example.dto.CreateBookRequestDto;
import com.example.dto.mapping.BookMapper;
import com.example.entity.Book;
import com.example.repository.BookRepository;
import com.example.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto bookRequestDto) {
        Book model = bookMapper.toModel(bookRequestDto);
        Book savedBook = bookRepository.save(model);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Book bookById = bookRepository.getBookById(id);
        return bookMapper.toDto(bookById);
    }

}
