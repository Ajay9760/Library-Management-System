package com.ajay.library.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.ajay.library.model.Book;
import com.ajay.library.model.BookCategory;

/**
 * Exercises SqliteBookRepository against a real on-disk SQLite file in a
 * JUnit-managed temp directory (not ":memory:" - this repository opens a
 * new connection per call, and SQLite's in-memory mode is a fresh empty
 * database per connection unless a shared-cache URI is used, so a real
 * temp file is what actually proves data survives across calls).
 */
public class SqliteBookRepositoryTest {

    private SqliteBookRepository repository;

    @BeforeEach
    void setup(@TempDir Path tempDir) {
        String jdbcUrl = "jdbc:sqlite:" + tempDir.resolve("test-books.db");
        repository = new SqliteBookRepository(jdbcUrl);
    }

    @Test
    void saveThenFindByIdReturnsTheSameBook() {
        Book book = new Book("B1", "Clean Code", "Robert C. Martin", BookCategory.TECHNOLOGY);
        repository.save(book);

        Optional<Book> found = repository.findById("B1");

        assertTrue(found.isPresent());
        assertEquals("Clean Code", found.get().getTitle());
        assertEquals("Robert C. Martin", found.get().getAuthor());
        assertEquals(BookCategory.TECHNOLOGY, found.get().getCategory());
        assertTrue(found.get().isAvailable());
    }

    @Test
    void availabilityIsPersistedAcrossConnections() throws Exception {
        Book book = new Book("B1", "T", "A", BookCategory.FICTION);
        book.borrow(); // flips isAvailable to false before saving
        repository.save(book);

        Optional<Book> found = repository.findById("B1");

        assertTrue(found.isPresent());
        assertFalse(found.get().isAvailable(), "Persisted availability should round-trip through the database");
    }

    @Test
    void savingWithSameIdUpsertsRatherThanDuplicating() {
        repository.save(new Book("B1", "First Title", "Author A", BookCategory.FICTION));
        repository.save(new Book("B1", "Second Title", "Author B", BookCategory.SCIENCE));

        assertEquals(1, repository.findAll().size());
        assertEquals("Second Title", repository.findById("B1").orElseThrow().getTitle());
    }

    @Test
    void findByIdReturnsEmptyWhenMissing() {
        assertTrue(repository.findById("does-not-exist").isEmpty());
    }

    @Test
    void deleteByIdRemovesTheBook() {
        repository.save(new Book("B1", "T", "A", BookCategory.FICTION));

        repository.deleteById("B1");

        assertTrue(repository.findById("B1").isEmpty());
    }

    @Test
    void clearRemovesAllBooks() {
        repository.save(new Book("B1", "T1", "A1", BookCategory.FICTION));
        repository.save(new Book("B2", "T2", "A2", BookCategory.SCIENCE));

        repository.clear();

        assertTrue(repository.findAll().isEmpty());
    }
}
