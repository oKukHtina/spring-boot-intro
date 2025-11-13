package com.example.repository.specification.impl;

import static com.example.constants.ApplicationConstant.AUTHOR_KEY;
import static com.example.constants.ApplicationConstant.TITLE_KEY;

import com.example.dto.BookSearchParametersDto;
import com.example.entity.Book;
import com.example.exception.InvalidProviderException;
import com.example.repository.specification.SpecificationBuilder;
import com.example.repository.specification.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {

    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto params) {
        Specification<Book> spec = Specification.where(null);

        boolean hasValidParam = false;

        if (ArrayUtils.isNotEmpty(params.authors())) {
            hasValidParam = true;
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(AUTHOR_KEY)
                    .getSpecification(params.authors()));
        }

        if (ArrayUtils.isNotEmpty(params.titles())) {
            hasValidParam = true;
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(TITLE_KEY)
                    .getSpecification(params.titles()));
        }

        if (!hasValidParam) {
            throw new InvalidProviderException(
                    "Invalid search parameter. Allowed: 'authors', 'titles'."
            );
        }

        return spec;
    }
}
