package src;
import src.model.Book;
import src.model.Patron;
import src.repository.BookRepository;
import src.repository.PatronRepository;
import src.service.LendingService;

/**
 * Main class for the Library Management System.
 */
public class Main {

    /**
     * Main method to run the application.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        // Create repositories for books and patrons
        BookRepository bookRepo = new BookRepository();
        PatronRepository patronRepo = new PatronRepository();

        // Create a lending service with the repositories
        LendingService lendingService = new LendingService(bookRepo, patronRepo);

        // Create a book and add it to the repository
        Book book1 = new Book("Clean Code", "Robert Martin", "56789", 2008);
        bookRepo.addBook(book1);

        // Create a patron and add it to the repository
        Patron patron1 = new Patron("1", "Alice", "alice@example.com");
        patronRepo.addPatron(patron1);

        // Checkout the book for the patron
        lendingService.checkoutBook("1", "12345");

        // Return the book (note: ISBN should not be null)
        lendingService.returnBook("12345");
    }
}