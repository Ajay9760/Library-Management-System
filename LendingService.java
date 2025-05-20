package src.service;

import java.util.Optional;
import java.util.logging.Logger;

import src.model.Book;
import src.model.Patron;
import src.repository.BookRepository;
import src.repository.PatronRepository;

/**
 * Service for managing book lending.
 */
public class LendingService {

    private static final Logger logger = Logger.getLogger(LendingService.class.getName());

    private final BookRepository bookRepo;
    private final PatronRepository patronRepo;

    /**
     * Creates a new LendingService instance.
     * @param bookRepo the book repository
     * @param patronRepo the patron repository
     */
    public LendingService(BookRepository bookRepo, PatronRepository patronRepo) {
        this.bookRepo = bookRepo;
        this.patronRepo = patronRepo;
    }

    /**
     * Checks out a book for a patron.
     * @param patronId the ID of the patron
     * @param isbn the ISBN of the book
     */
    public void checkoutBook(String patronId, String isbn) {
        Optional<Patron> patronOptional = patronRepo.getPatronById(patronId);
        Optional<Book> bookOptional = bookRepo.getBookByIsbn(isbn);

        if (patronOptional.isPresent() && bookOptional.isPresent()) {
            Patron patron = patronOptional.get();
            Book book = bookOptional.get();

            if (book.isAvailable()) {
                book.setAvailable(book, false);
                patron.getBorrowedBooks().add(book);
                logger.info(patron.getName() + " checked out book: " + book.getTitle());
            } else {
                logger.warning("Book is not available: " + book.getTitle());
            }
        } else {
            logger.warning("Patron or book not found");
        }
    }

    /**
     * Returns a book.
     * @param isbn the ISBN of the book
     */
    public void returnBook(String isbn) {
        Optional<Book> bookOptional = bookRepo.getBookByIsbn(isbn);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();

            if (!book.isAvailable()) {
                book.setAvailable(book, true);
                Patron patron = getPatronWhoBorrowedBook(book);
                if (patron != null) {
                    patron.getBorrowedBooks().remove(book);
                    logger.info(patron.getName() + " returned book: " + book.getTitle());
                } else {
                    logger.warning("Patron who borrowed book not found");
                }
            } else {
                logger.warning("Book is already available: " + book.getTitle());
            }
        } else {
            logger.warning("Book not found");
        }
    }

    private Patron getPatronWhoBorrowedBook(Book book) {
        for (Patron patron : patronRepo.getAllPatrons()) {
            if (patron.getBorrowedBooks().contains(book)) {
                return patron;
            }
        }
        return null;
    }

    public static Logger getLogger() {
        return logger;
    }

    public BookRepository getBookRepo() {
        return bookRepo;
    }

    public PatronRepository getPatronRepo() {
        return patronRepo;
    }
}