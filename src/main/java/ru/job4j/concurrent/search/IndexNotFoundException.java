package ru.job4j.concurrent.search;

public class IndexNotFoundException extends RuntimeException {

    public IndexNotFoundException(String message) {
        super(message);
    }
}
