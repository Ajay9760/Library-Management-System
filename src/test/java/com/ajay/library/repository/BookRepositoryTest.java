package com.ajay.library.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ajay.library.model.Book;
import com.ajay.library.model.BookCategory;

public class BookRepositoryTest {

    private BookRepository repository;

    @BeforeEach
    void setup() {
        repository = new BookRepository();
    }

    @Test
    void saveThenFindByIdReturnsTheSameBook() {
        Book book = new Book("B1", "Clean Code", "Robert C. Martin", BookCategory.TECHNOLOGY);
        repository.save(book);

        Optional<Book> found = repository.findById("B1");

        assertTrue(found.isPresent());
        assertEquals(book, found.get());
        assertEquals("Clean Code", found.get().getTitle());
    }

    @Test
    void findByIdReturnsEmptyWhenMissing() {
        assertTrue(repository.findById("does-not-exist").isEmpty());
    }

    @Test
    void saveWithSameIdOverwritesExistingEntry() {
        repository.save(new Book("B1", "First Title", "Author A", BookCategory.FICTION));
        repository.save(new Book("B1", "Second Title", "Author B", BookCategory.SCIENCE));

        Optional<Book> found = repository.findById("B1");

        assertTrue(found.isPresent());
        assertEquals("Second Title", found.get().getTitle());
        assertEquals(1, repository.findAll().size(), "Overwriting by id should not create a duplicate entry");
    }

    @Test
    void findAllReturnsEveryStoredBook() {
        repository.save(new Book("B1", "T1", "A1", BookCategory.FICTION));
        repository.save(new Book("B2", "T2", "A2", BookCategory.SCIENCE));

        assertEquals(2, repository.findAll().size());
    }

    @Test
    void deleteByIdRemovesTheBook() {
        repository.save(new Book("B1", "T1", "A1", BookCategory.FICTION));

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
