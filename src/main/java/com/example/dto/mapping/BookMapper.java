package com.example.dto.mapping;

import com.example.dto.BookDto;
import com.example.dto.CreateBookRequestDto;
import com.example.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto bookRequestDto);
}
