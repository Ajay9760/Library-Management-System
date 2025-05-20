package src;
import src.model.Book;
import src.model.Patron;
import src.repository.BookRepository;
import src.repository.PatronRepository;
import src.service.LendingService;


public class Main {

    public static void main(String[] args) {
        BookRepository bookRepo = new BookRepository();
        PatronRepository patronRepo = new PatronRepository();

        LendingService lendingService = new LendingService(bookRepo, patronRepo);

        Book book1 = new Book("Clean Code", "Robert Martin", "12345", 2008);
        bookRepo.addBook(book1);

        Patron patron1 = new Patron("1", "Alice", "alice@example.com");
        patronRepo.addPatron(patron1);

        lendingService.checkoutBook("1", "12345");

        lendingService.returnBook("12345");
    }
}
