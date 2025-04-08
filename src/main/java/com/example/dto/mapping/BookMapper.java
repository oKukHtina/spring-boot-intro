package com.example.dto.mapping;

import com.example.dto.BookDto;
import com.example.dto.CreateBookRequestDto;
import com.example.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto bookRequestDto);
}
