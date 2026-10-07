package com.ajay.library.exception;

/**
 * Common base for checked, library-domain exceptions — conditions that are
 * expected outcomes of normal operation (a book being unavailable, a borrow
 * limit being reached, a return not matching a real loan) and that calling
 * code is expected to handle, not just let propagate.
 *
 * {@link InvalidInputException} deliberately does NOT extend this class: it
 * signals a programming/contract error (a null or malformed argument) rather
 * than a legitimate business outcome, so it stays an unchecked
 * {@link RuntimeException} — callers shouldn't be forced to catch it on
 * every call, the same way they aren't forced to catch
 * NullPointerException.
 */
public abstract class LibraryException extends Exception {
    protected LibraryException(String message) {
        super(message);
    }
}
