package com.example.service.impl;

import com.example.dto.BookDto;
import com.example.dto.CreateBookRequestDto;
import com.example.dto.mapping.BookMapper;
import com.example.entity.Book;
import com.example.repository.BookRepository;
import com.example.service.BookService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public BookDto save(CreateBookRequestDto bookRequestDto) {
        Book model = BookMapper.INSTANCE.toModel(bookRequestDto);
        Book savedBook = bookRepository.save(model);
        return BookMapper.INSTANCE.toDto(savedBook);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(BookMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto getBookById(Long id) {
        Book bookById = bookRepository.getBookById(id);
        return BookMapper.INSTANCE.toDto(bookById);
    }

}
