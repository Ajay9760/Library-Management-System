package com.ajay.library.model;

import java.util.Objects;

import com.ajay.library.exception.BookNotAvailableException;
import com.ajay.library.interfaces.IBorrowable;
import com.ajay.library.interfaces.ICategorizable;

public class Book implements IBorrowable, ICategorizable {
    private final String id;
    private String title;
    private String author;
    private boolean isAvailable;
    private BookCategory category;

    public Book(String id, String title, String author, BookCategory category) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Book id cannot be blank");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Book title cannot be blank");
        if (author == null || author.isBlank()) throw new IllegalArgumentException("Book author cannot be blank");
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category == null ? BookCategory.OTHER : category;
        this.isAvailable = true;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return isAvailable; }
    public BookCategory getCategory() { return category; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(BookCategory category) { this.category = category; }

    public void setAvailable(boolean available) { // fixed: no Book parameter
        this.isAvailable = available;
    }

    @Override
    public void borrow() throws BookNotAvailableException {
        if (!isAvailable) {
            throw new BookNotAvailableException("Book not available: " + title);
        }
        this.isAvailable = false;
    }

    @Override
    public void returnBook() {
        this.isAvailable = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\''+
                ", title='" + title + '\''+
                ", author='" + author + '\'' +
                ", isAvailable=" + isAvailable +
                ", category=" + category +
                '}';
    }
}
