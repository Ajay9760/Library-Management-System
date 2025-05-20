package src.model;

import java.util.*;

public class Patron {
    
    public Patron() {
  }
    private String id;
    private String name;

    private String email;

    private List<Book> borrowedBooks = new ArrayList<>();
    public Patron(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }  
    public Patron(String id, String name, String email, List<Book> borrowedBooks) {
      this.id = id;
      this.name = name;
      this.email = email;
      this.borrowedBooks = borrowedBooks;
    }
    public String getId() { return id; }

    public String getName() { return name; }

    public String getEmail() { return email; }

    public List<Book> getBorrowedBooks() { return borrowedBooks; }

    public void setId(String id) {
      this.id = id;
    }

    public void setName(String name) {
      this.name = name;
    }

    public void setEmail(String email) {
      this.email = email;
    }
    public void setBorrowedBooks(List<Book> borrowedBooks) {
      this.borrowedBooks = borrowedBooks;
    }
}
