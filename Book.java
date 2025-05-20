package src.model;

import java.util.Optional;

public class Book implements enter {
    private String title;
    private String author;
    private String isbn;
    private int publicationYear;
    private boolean isAvailable = true;

    public Book(String title, String author, String isbn, int publicationYear) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
    }

    @Override
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public int getPublicationYear() { return publicationYear; }
    @Override
    public boolean isAvailable() { return isAvailable; }

    public void setAvailable(Book book, boolean available) { book.isAvailable = available; }

    public static void put(String isbn2, enter book) {
      
      throw new UnsupportedOperationException("Unimplemented method 'put'");
    }

    public Optional<Patron> getBorrowedBooks() {
      throw new UnsupportedOperationException("Unimplemented method 'getBorrowedBooks'");
    }
}