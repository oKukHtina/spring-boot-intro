package com.example.service.impl;

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

    @Override
    public Book save(Book bookEntity) {
        return bookRepository.save(bookEntity);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
