package com.ajay.library.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.ajay.library.exception.BookNotAvailableException;
import com.ajay.library.exception.BorrowLimitExceededException;

public class Patron {
    private final String id;
    private String name;
    private final List<BorrowRecord> borrowedBooks = new ArrayList<>();

    public static final int DEFAULT_BORROW_LIMIT = 5;

    public Patron(String id, String name) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Patron id cannot be blank");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Patron name cannot be blank");
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<BorrowRecord> getBorrowedBooks() {
        return Collections.unmodifiableList(borrowedBooks);
    }

    public void borrowBook(Book book) throws BorrowLimitExceededException, BookNotAvailableException {
        if (borrowedBooks.size() >= DEFAULT_BORROW_LIMIT) {
            throw new BorrowLimitExceededException("Patron has reached max borrow limit: " + DEFAULT_BORROW_LIMIT);
        }
        book.borrow();
        borrowedBooks.add(BorrowRecord.start(book, LocalDate.now()));
    }

    public void returnBook(Book book) {
        Iterator<BorrowRecord> iterator = borrowedBooks.iterator();
        while (iterator.hasNext()) {
            BorrowRecord record = iterator.next();
            if (record.getBook().equals(book)) {
                iterator.remove();
                break;
            }
        }
        book.returnBook();
    }

    public long totalOutstandingFines(LocalDate referenceDate) {
        return borrowedBooks.stream()
            .mapToLong(r -> r.calculateFine(referenceDate))
            .sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patron)) return false;
        Patron patron = (Patron) o;
        return id.equals(patron.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Patron{" +
                "id='" + id + "'" +
                ", name='" + name + "'" +
                ", borrowedBooks=" + borrowedBooks.size() +
                '}';

    }
}
