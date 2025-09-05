package com.ajay.library;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ajay.library.exception.BookNotAvailableException;
import com.ajay.library.exception.BorrowLimitExceededException;
import com.ajay.library.exception.InvalidInputException;
import com.ajay.library.model.Book;
import com.ajay.library.model.BookCategory;
import com.ajay.library.model.Patron;
import com.ajay.library.repository.BookRepository;
import com.ajay.library.repository.PatronRepository;
import com.ajay.library.service.LibraryService;

public class LibraryServiceTest {

    private BookRepository bookRepository;
    private PatronRepository patronRepository;
    private LibraryService service;

    @BeforeEach
    void setup() {
        bookRepository = new BookRepository();
        patronRepository = new PatronRepository();
        service = new LibraryService(bookRepository, patronRepository);
    }

    @Test
    void borrowAndReturnFlow() throws Exception {
        Book book = new Book("B1", "Test", "Auth", BookCategory.FICTION);
        Patron patron = new Patron("P1", "Pat");
        service.registerBook(book);
        service.registerPatron(patron);

        service.borrowBook("P1", "B1");
        assertFalse(book.isAvailable(), "Book should be marked unavailable after borrow");

        service.returnBook("P1", "B1");
        assertTrue(book.isAvailable(), "Book should be marked available after return");
    }

    @Test
    void borrowLimitEnforced() throws Exception {
        Patron patron = new Patron("P1", "Pat");
        service.registerPatron(patron);
        for (int i = 1; i <= 5; i++) {
            Book b = new Book("B" + i, "T" + i, "A" + i, BookCategory.OTHER);
            service.registerBook(b);
            service.borrowBook("P1", "B" + i);
        }
        Book extra = new Book("B6", "T6", "A6", BookCategory.OTHER);
        service.registerBook(extra);
        assertThrows(BorrowLimitExceededException.class, () -> service.borrowBook("P1", "B6"));
    }

    @Test
    void notAvailableThrows() throws Exception {
        Patron patron = new Patron("P1", "Pat");
        service.registerPatron(patron);
        Book book = new Book("B1", "T", "A", BookCategory.SCIENCE);
        service.registerBook(book);
        service.borrowBook("P1", "B1");

        Patron other = new Patron("P2", "Other");
        service.registerPatron(other);
        assertThrows(BookNotAvailableException.class, () -> service.borrowBook("P2", "B1"));
    }

    @Test
    void calculateFines() throws Exception {
        Patron patron = new Patron("P1", "Pat");
        service.registerPatron(patron);
        Book book = new Book("B1", "T", "A", BookCategory.HISTORY);
        service.registerBook(book);
        service.borrowBook("P1", "B1");

        long fine = service.calculatePatronFine("P1", LocalDate.now().plusDays(20));
        assertEquals(60L, fine); // 20 - 14 = 6 days late * ₹10
    }

    @Test
    void invalidIdsThrow() {
        assertThrows(InvalidInputException.class, () -> service.borrowBook("X", "Y"));
    }
}
