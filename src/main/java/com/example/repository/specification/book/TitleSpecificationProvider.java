package com.example.repository.specification.book;

import static com.example.constants.ApplicationConstant.TITLE_KEY;

import com.example.entity.Book;
import com.example.repository.specification.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public String getKey() {
        return TITLE_KEY;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get(TITLE_KEY)
                .in(Arrays.stream(params).toArray());
    }
}
