package com.ajay.library.repository;

import java.util.*;

import com.ajay.library.model.Book;

public class BookRepository {
    private final Map<String, Book> books = new HashMap<>();

    public Book save(Book book) {
        books.put(book.getId(), book);
        return book;
    }

    public Optional<Book> findById(String id) {
        return Optional.ofNullable(books.get(id));
    }

    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    public void deleteById(String id) {
        books.remove(id);
    }

    public void clear() {
        books.clear();
    }
}
