package com.example.service.impl;

import com.example.dto.BookDto;
import com.example.dto.BookSearchParametersDto;
import com.example.dto.CreateBookRequestDto;
import com.example.dto.mapping.BookMapper;
import com.example.entity.Book;
import com.example.repository.BookRepository;
import com.example.repository.specification.impl.BookSpecificationBuilder;
import com.example.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

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
        Book foundedBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book was not found by id : " + id));
        bookMapper.updateBookFromDto(bookDto, foundedBook);

        return bookMapper.toDto(bookRepository.save(foundedBook));
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto searchParameters) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
