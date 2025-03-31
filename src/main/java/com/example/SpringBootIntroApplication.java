package com.example;

import com.example.entity.BookEntity;
import com.example.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootIntroApplication implements CommandLineRunner {

    private final BookService bookService;

    @Autowired
    public SpringBootIntroApplication(BookService bookService) {
        this.bookService = bookService;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootIntroApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setTitle("Book 1");
        bookEntity.setAuthor("Author 1");
        bookEntity.setPrice(BigDecimal.ZERO);
        bookEntity.setDescription("sadasdad");
        bookEntity.setIsbn("123");
        bookEntity.setCoverImage("sd");
        BookEntity save = bookService.save(bookEntity);
        System.out.println(save);

        bookService.findAll().forEach(System.out::println);
    }
}
