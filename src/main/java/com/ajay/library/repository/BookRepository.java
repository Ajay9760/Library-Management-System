package com.ajay.library.repository;

import java.util.List;
import java.util.Optional;

import com.ajay.library.model.Book;

/**
 * Storage abstraction for {@link Book} records. Keeping this as an interface
 * lets {@link com.ajay.library.service.LibraryService} stay unaware of
 * whether books live in memory ({@link InMemoryBookRepository}) or in a
 * real database ({@link SqliteBookRepository}).
 */
public interface BookRepository {
    Book save(Book book);

    Optional<Book> findById(String id);

    List<Book> findAll();

    void deleteById(String id);

    void clear();
}
