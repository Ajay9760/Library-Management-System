package com.ajay.library.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BorrowRecord {
    private final Book book;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;

    public static final int DEFAULT_LENDING_DAYS = 14;
    public static final long FINE_PER_DAY = 10L; // ₹10/day

    public BorrowRecord(Book book, LocalDate borrowDate, LocalDate dueDate) {
        this.book = book;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    public static BorrowRecord start(Book book, LocalDate borrowDate) {
        LocalDate due = borrowDate.plusDays(DEFAULT_LENDING_DAYS);
        return new BorrowRecord(book, borrowDate, due);
    }

    public long calculateFine(LocalDate referenceDate) {
        if (referenceDate.isAfter(dueDate)) {
            long days = ChronoUnit.DAYS.between(dueDate, referenceDate);
            return days * FINE_PER_DAY;
        }
        return 0L;
    }

    public Book getBook() { return book; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
}
