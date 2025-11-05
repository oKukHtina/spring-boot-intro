package com.example.service.impl;

import com.example.dto.BookDto;
import com.example.dto.CreateBookRequestDto;
import com.example.dto.mapping.BookMapper;
import com.example.entity.Book;
import com.example.repository.BookRepository;
import com.example.service.BookService;
import jakarta.persistence.EntityNotFoundException;
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
        bookRepository.save(model);
        return bookMapper.toDto(model);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Book bookById = bookRepository.getById(id);
        return bookMapper.toDto(bookById);
    }

    @Override
    public BookDto updateBook(Long id, CreateBookRequestDto bookDto) {
        Book foundedbook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book was not found by id : " + id));

        foundedbook.setTitle(bookDto.getTitle());
        foundedbook.setAuthor(bookDto.getAuthor());
        foundedbook.setPrice(bookDto.getPrice());
        foundedbook.setIsbn(bookDto.getIsbn());
        foundedbook.setCoverImage(bookDto.getCoverImage());
        foundedbook.setDescription(bookDto.getDescription());

        return bookMapper.toDto(bookRepository.save(foundedbook));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
