package com.example.repository.specification.impl;

import static com.example.constants.ApplicationConstant.AUTHOR_KEY;
import static com.example.constants.ApplicationConstant.TITLE_KEY;

import com.example.dto.BookSearchParametersDto;
import com.example.entity.Book;
import com.example.exception.InvalidProviderException;
import com.example.repository.specification.SpecificationBuilder;
import com.example.repository.specification.SpecificationProviderManager;
import java.util.ArrayList;
import java.util.List;
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
        List<Specification<Book>> specs = new ArrayList<>();

        if (ArrayUtils.isNotEmpty(params.authors())) {
            specs.add(specificationProviderManager
                    .getSpecificationProvider(AUTHOR_KEY)
                    .getSpecification(params.authors()));
        }

        if (ArrayUtils.isNotEmpty(params.titles())) {
            specs.add(specificationProviderManager
                    .getSpecificationProvider(TITLE_KEY)
                    .getSpecification(params.titles()));
        }

        if (specs.isEmpty()) {
            throw new InvalidProviderException(
                    "Invalid search parameter. Allowed: 'authors', 'titles'."
            );
        }

        return specs.stream()
                .reduce(Specification::and)
                .orElseThrow(() -> new InvalidProviderException("No valid specification found"));
    }
}
