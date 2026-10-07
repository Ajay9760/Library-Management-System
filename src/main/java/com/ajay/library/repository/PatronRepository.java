package com.ajay.library.repository;

import java.util.List;
import java.util.Optional;

import com.ajay.library.model.Patron;

/**
 * Storage abstraction for {@link Patron} records.
 * See {@link InMemoryPatronRepository} and {@link SqlitePatronRepository}.
 */
public interface PatronRepository {
    Patron save(Patron patron);

    Optional<Patron> findById(String id);

    List<Patron> getAllPatrons();

    void deleteById(String id);

    void clear();
}
