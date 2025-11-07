package com.example.repository.specification.impl;

import com.example.dto.BookSearchParametersDto;
import com.example.entity.Book;
import com.example.repository.specification.SpecificationBuilder;
import com.example.repository.specification.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String AUTHOR = "author";
    private static final String TITLE = "title";
    private static final int ZERO = 0;

    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto bookSearchParametersDto) {
        Specification<Book> specification = Specification.where(null);

        if (bookSearchParametersDto.authors() != null
                && bookSearchParametersDto.authors().length > ZERO) {
            specification = specification.and(
                    bookSpecificationProviderManager.getSpecificationProvider(AUTHOR)
                            .getSpecification(bookSearchParametersDto.authors())
            );
        }

        if (bookSearchParametersDto.titles() != null
                && bookSearchParametersDto.titles().length > ZERO) {
            specification = specification.and(
                    bookSpecificationProviderManager.getSpecificationProvider(TITLE)
                    .getSpecification(bookSearchParametersDto.titles())
            );
        }
        return specification;
    }
}
