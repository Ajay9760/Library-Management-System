package src.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import src.model.Book;

public class BookRepository {
    private Map<String, Book> books = new HashMap<>();
    public void addBook(Book book) {
        books.put(book.getIsbn(), book);
    }
    public void removeBook(String isbn) {
        books.remove(isbn);
    }
    public Optional<Book> getBookByIsbn(String isbn) {
        return Optional.ofNullable(books.get(isbn));
    }
    public List<Book> searchByTitle(String title) {
        return books.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .toList();
    }
    public List<Book> searchByAuthor(String author) {
        return books.values().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .toList();
    }
    public Collection<Book> getAllBooks() {
        return books.values();
    }
}
