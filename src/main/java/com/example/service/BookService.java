package com.example.service;

import com.example.entity.Book;
import java.util.List;

public interface BookService {
    Book save(Book bookEntity);

    List<Book> findAll();
}
