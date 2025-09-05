package com.ajay.library.service;

import java.time.LocalDate;

import com.ajay.library.exception.BookNotAvailableException;
import com.ajay.library.exception.BorrowLimitExceededException;
import com.ajay.library.exception.InvalidInputException;
import com.ajay.library.model.Book;
import com.ajay.library.model.Patron;
import com.ajay.library.repository.BookRepository;
import com.ajay.library.repository.PatronRepository;

public class LibraryService {

    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;

    public LibraryService(BookRepository bookRepository, PatronRepository patronRepository) {
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
    }

    public void registerBook(Book book) {
        if (book == null) throw new InvalidInputException("Book cannot be null");
        bookRepository.save(book);
    }

    public void registerPatron(Patron patron) {
        if (patron == null) throw new InvalidInputException("Patron cannot be null");
        patronRepository.save(patron);
    }

    public void borrowBook(String patronId, String bookId) throws BorrowLimitExceededException, BookNotAvailableException {
        Patron patron = patronRepository.findById(patronId).orElseThrow(() -> new InvalidInputException("Patron not found: " + patronId));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new InvalidInputException("Book not found: " + bookId));
        patron.borrowBook(book);
    }

    public void returnBook(String patronId, String bookId) {
        Patron patron = patronRepository.findById(patronId).orElseThrow(() -> new InvalidInputException("Patron not found: " + patronId));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new InvalidInputException("Book not found: " + bookId));
        patron.returnBook(book);
    }

    public long calculatePatronFine(String patronId, LocalDate referenceDate) {
        Patron patron = patronRepository.findById(patronId).orElseThrow(() -> new InvalidInputException("Patron not found: " + patronId));
        return patron.totalOutstandingFines(referenceDate);
    }
}
