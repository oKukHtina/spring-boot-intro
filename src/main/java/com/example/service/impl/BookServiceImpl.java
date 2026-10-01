package com.example.service.impl;

import com.example.dto.request.BookSearchParametersDto;
import com.example.dto.request.CreateBookRequestDto;
import com.example.dto.response.BookResponseDto;
import com.example.dto.response.BookResponseDtoWithoutCategoryIds;
import com.example.entity.Book;
import com.example.entity.Category;
import com.example.exception.CategoryNotFoundException;
import com.example.mapper.BookMapper;
import com.example.repository.BookRepository;
import com.example.repository.CategoryRepository;
import com.example.repository.specification.impl.BookSpecificationBuilder;
import com.example.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookResponseDto save(CreateBookRequestDto bookRequestDto) {
        Book model = bookMapper.toModel(bookRequestDto);
        Set<Category> categories = findCategoriesByIds(bookRequestDto.getCategoryIds());
        model.setCategories(categories);

        bookRepository.save(model);
        return bookMapper.toDto(model);
    }

    @Override
    public Page<BookResponseDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookResponseDto getBookById(Long id) {
        return bookMapper.toDto(findBookById(id));
    }

    @Override
    public BookResponseDto updateBook(Long id, CreateBookRequestDto bookDto) {
        Book foundedBook = findBookById(id);
        Set<Category> categories = findCategoriesByIds(bookDto.getCategoryIds());

        bookMapper.updateBookFromDto(bookDto, foundedBook);
        foundedBook.setCategories(categories);

        return bookMapper.toDto(bookRepository.save(foundedBook));
    }

    @Override
    public Page<BookResponseDto> search(
            BookSearchParametersDto searchParameters,
            Pageable pageable
    ) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification, pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.delete(findBookById(id));
    }

    @Override
    public Page<BookResponseDtoWithoutCategoryIds> findBooksByCategoryId(
            Long id,
            Pageable pageable
    ) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category was not found by id : " + id
                ));

        return bookRepository.findAllByCategoriesId(id, pageable)
                .map(bookMapper::toDtoWithoutCategoryIds);
    }

    private Set<Category> findCategoriesByIds(List<Long> categoryIds) {
        List<Category> categories = categoryRepository.findAllById(categoryIds);

        if (categories.size() != categoryIds.size()) {
            throw new CategoryNotFoundException(
                    "One or more categories were not found"
            );
        }

        return new HashSet<>(categories);
    }

    private Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book was not found by id : " + id
                ));
    }
}
