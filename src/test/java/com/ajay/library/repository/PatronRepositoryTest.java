package com.ajay.library.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ajay.library.model.Patron;

public class PatronRepositoryTest {

    private PatronRepository repository;

    @BeforeEach
    void setup() {
        repository = new PatronRepository();
    }

    @Test
    void saveThenFindByIdReturnsTheSamePatron() {
        Patron patron = new Patron("P1", "Ajay");
        repository.save(patron);

        Optional<Patron> found = repository.findById("P1");

        assertTrue(found.isPresent());
        assertEquals(patron, found.get());
        assertEquals("Ajay", found.get().getName());
    }

    @Test
    void findByIdReturnsEmptyWhenMissing() {
        assertTrue(repository.findById("does-not-exist").isEmpty());
    }

    @Test
    void getAllPatronsReturnsEveryStoredPatron() {
        repository.save(new Patron("P1", "Ajay"));
        repository.save(new Patron("P2", "Priya"));

        assertEquals(2, repository.getAllPatrons().size());
    }

    @Test
    void deleteByIdRemovesThePatron() {
        repository.save(new Patron("P1", "Ajay"));

        repository.deleteById("P1");

        assertTrue(repository.findById("P1").isEmpty());
    }

    @Test
    void clearRemovesAllPatrons() {
        repository.save(new Patron("P1", "Ajay"));
        repository.save(new Patron("P2", "Priya"));

        repository.clear();

        assertTrue(repository.getAllPatrons().isEmpty());
    }
}
