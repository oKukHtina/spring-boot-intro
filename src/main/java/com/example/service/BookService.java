package com.example.service;

import com.example.entity.BookEntity;
import java.util.List;

public interface BookService {
    BookEntity save(BookEntity bookEntity);

    List<BookEntity> findAll();
}
