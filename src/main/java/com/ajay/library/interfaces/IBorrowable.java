package com.ajay.library.interfaces;

import com.ajay.library.exception.BookNotAvailableException;

public interface IBorrowable {
    void borrow() throws BookNotAvailableException;
    void returnBook();
    boolean isAvailable();
}
