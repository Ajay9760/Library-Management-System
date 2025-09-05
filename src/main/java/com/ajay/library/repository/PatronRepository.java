package com.ajay.library.repository;

import java.util.*;

import com.ajay.library.model.Patron;

public class PatronRepository {
    private final Map<String, Patron> patrons = new HashMap<>();

    public Patron save(Patron patron) {
        patrons.put(patron.getId(), patron);
        return patron;
    }

    public Optional<Patron> findById(String id) {
        return Optional.ofNullable(patrons.get(id));
    }

    public List<Patron> getAllPatrons() { // Implemented
        return new ArrayList<>(patrons.values());
    }

    public void deleteById(String id) {
        patrons.remove(id);
    }

    public void clear() {
        patrons.clear();
    }
}
