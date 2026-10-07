package com.ajay.library.repository;

import java.util.*;

import com.ajay.library.model.Book;

/**
 * In-memory implementation of {@link BookRepository} backed by a HashMap.
 * Simple and fast, but not durable - data is lost when the process exits.
 * Suited to tests and the demo app; see {@link SqliteBookRepository} for a
 * persisted alternative.
 */
public class InMemoryBookRepository implements BookRepository {
    private final Map<String, Book> books = new HashMap<>();

    @Override
    public Book save(Book book) {
        books.put(book.getId(), book);
        return book;
    }

    @Override
    public Optional<Book> findById(String id) {
        return Optional.ofNullable(books.get(id));
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    @Override
    public void deleteById(String id) {
        books.remove(id);
    }

    @Override
    public void clear() {
        books.clear();
    }
}
