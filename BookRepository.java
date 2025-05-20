package src.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import src.model.Book;

/**
 * Repository for managing books.
 */
public class BookRepository {

    private Map<String, Book> books = new HashMap<>();

    /**
     * Adds a book to the repository.
     * @param book the book to add
     */
    public void addBook(Book book) {
        books.put(book.getIsbn(), book);
    }

    /**
     * Removes a book from the repository by ISBN.
     * @param isbn the ISBN of the book to remove
     */
    public void removeBook(String isbn) {
        books.remove(isbn);
    }

    /**
     * Retrieves a book by ISBN.
     * @param isbn the ISBN of the book to retrieve
     * @return the book, or an empty Optional if not found
     */
    public Optional<Book> getBookByIsbn(String isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    /**
     * Searches for books by title.
     * @param title the title to search for
     * @return a list of matching books
     */
    public List<Book> searchByTitle(String title) {
        return books.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .toList();
    }

    /**
     * Searches for books by author.
     * @param author the author to search for
     * @return a list of matching books
     */
    public List<Book> searchByAuthor(String author) {
        return books.values().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .toList();
    }

    /**
     * Retrieves all books in the repository.
     * @return a collection of all books
     */
    public Collection<Book> getAllBooks() {
        return books.values();
    }
}