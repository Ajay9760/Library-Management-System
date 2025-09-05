package com.ajay.library;

import java.time.LocalDate;

import com.ajay.library.exception.BookNotAvailableException;
import com.ajay.library.exception.BorrowLimitExceededException;
import com.ajay.library.model.Book;
import com.ajay.library.model.BookCategory;
import com.ajay.library.model.Patron;
import com.ajay.library.repository.BookRepository;
import com.ajay.library.repository.PatronRepository;
import com.ajay.library.service.LibraryService;

public class App {
    public static void main(String[] args) throws Exception {
        BookRepository bookRepo = new BookRepository();
        PatronRepository patronRepo = new PatronRepository();
        LibraryService service = new LibraryService(bookRepo, patronRepo);

        Book b1 = new Book("B1", "Clean Code", "Robert C. Martin", BookCategory.TECHNOLOGY);
        Book b2 = new Book("B2", "The Hobbit", "J.R.R. Tolkien", BookCategory.FICTION);
        service.registerBook(b1);
        service.registerBook(b2);

        Patron p1 = new Patron("P1", "Ajay");
        service.registerPatron(p1);

        try {
            service.borrowBook("P1", "B1");
            System.out.println("Borrowed B1");

            long fine = service.calculatePatronFine("P1", LocalDate.now().plusDays(20));
            System.out.println("Fine after 20 days: ₹" + fine);

            service.returnBook("P1", "B1");
            System.out.println("Returned B1");
        } catch (BorrowLimitExceededException | BookNotAvailableException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
