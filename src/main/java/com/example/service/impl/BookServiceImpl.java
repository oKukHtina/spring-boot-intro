package com.example.service.impl;

import com.example.entity.BookEntity;
import com.example.repository.BookRepository;
import com.example.service.BookService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookEntity save(BookEntity bookEntity) {
        return bookRepository.save(bookEntity);
    }

    @Override
    public List<BookEntity> findAll() {
        return bookRepository.findAll();
    }
}
