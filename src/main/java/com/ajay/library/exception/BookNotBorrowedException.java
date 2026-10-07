package com.ajay.library.exception;

/**
 * Thrown when a patron attempts to return a book that they do not currently
 * have on loan. Prevents a non-borrower from flipping a book's availability
 * state and masking who actually holds it.
 */
public class BookNotBorrowedException extends LibraryException {
    public BookNotBorrowedException(String message) {
        super(message);
    }
}
