package com.ajay.library.repository;

import java.util.*;

import com.ajay.library.model.Patron;

/**
 * In-memory implementation of {@link PatronRepository} backed by a HashMap.
 * See {@link SqlitePatronRepository} for a persisted alternative.
 */
public class InMemoryPatronRepository implements PatronRepository {
    private final Map<String, Patron> patrons = new HashMap<>();

    @Override
    public Patron save(Patron patron) {
        patrons.put(patron.getId(), patron);
        return patron;
    }

    @Override
    public Optional<Patron> findById(String id) {
        return Optional.ofNullable(patrons.get(id));
    }

    @Override
    public List<Patron> getAllPatrons() {
        return new ArrayList<>(patrons.values());
    }

    @Override
    public void deleteById(String id) {
        patrons.remove(id);
    }

    @Override
    public void clear() {
        patrons.clear();
    }
}
