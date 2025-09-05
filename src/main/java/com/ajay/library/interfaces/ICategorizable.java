package com.ajay.library.interfaces;

import com.ajay.library.model.BookCategory;

public interface ICategorizable {
    BookCategory getCategory();
    void setCategory(BookCategory category);
}
