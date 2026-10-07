package com.ajay.library.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.ajay.library.model.Patron;

public class SqlitePatronRepositoryTest {

    private SqlitePatronRepository repository;

    @BeforeEach
    void setup(@TempDir Path tempDir) {
        String jdbcUrl = "jdbc:sqlite:" + tempDir.resolve("test-patrons.db");
        repository = new SqlitePatronRepository(jdbcUrl);
    }

    @Test
    void saveThenFindByIdReturnsTheSamePatron() {
        repository.save(new Patron("P1", "Ajay"));

        Optional<Patron> found = repository.findById("P1");

        assertTrue(found.isPresent());
        assertEquals("Ajay", found.get().getName());
    }

    @Test
    void loadedPatronHasNoBorrowedBooks() {
        // Documents the known scope limit: loan state isn't persisted,
        // so a patron loaded from SQLite always starts with an empty list.
        repository.save(new Patron("P1", "Ajay"));

        Patron loaded = repository.findById("P1").orElseThrow();

        assertTrue(loaded.getBorrowedBooks().isEmpty());
    }

    @Test
    void savingWithSameIdUpsertsRatherThanDuplicating() {
        repository.save(new Patron("P1", "Original Name"));
        repository.save(new Patron("P1", "Updated Name"));

        assertEquals(1, repository.getAllPatrons().size());
        assertEquals("Updated Name", repository.findById("P1").orElseThrow().getName());
    }

    @Test
    void findByIdReturnsEmptyWhenMissing() {
        assertTrue(repository.findById("does-not-exist").isEmpty());
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
