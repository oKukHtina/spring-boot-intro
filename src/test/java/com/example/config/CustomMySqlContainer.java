package com.example.config;

import org.testcontainers.containers.MySQLContainer;

public class CustomMySqlContainer extends MySQLContainer<CustomMySqlContainer> {
    private static final String DB_IMAGE = "mysql:8.4";
    private static final CustomMySqlContainer CONTAINER = new CustomMySqlContainer();

    private CustomMySqlContainer() {
        super(DB_IMAGE);
        withDatabaseName("book_store");
        withUsername("bookstore");
        withPassword("password");
    }

    public static CustomMySqlContainer getInstance() {
        return CONTAINER;
    }
}
