# 📚 Library Management System

A Core Java library management system built with Maven: borrowing limits, due-date tracking,
fine calculation, SQLite-backed persistence, and a JUnit 5 test suite covering the real
business rules (including the edge cases below).

This is a portfolio project — built to demonstrate clean OOP design, honest test coverage,
and deliberate design tradeoffs, not a production system.

## ✨ Features

- **Book management** — add/list books, track availability, categorize by genre
- **Patron management** — register patrons, enforce a per-patron borrow limit
- **Borrowing workflow** — borrow/return with due dates and late-return fines
- **Persistence** — `BookRepository`/`PatronRepository` are interfaces with two
  implementations: an in-memory one (`InMemoryBookRepository`, `InMemoryPatronRepository`)
  and a real SQLite-backed one (`SqliteBookRepository`, `SqlitePatronRepository`) using
  plain JDBC with `PreparedStatement`s throughout
- **Error handling** — a `LibraryException` hierarchy separates expected business outcomes
  (book unavailable, borrow limit reached, return without an active loan) from programming
  errors (`InvalidInputException`, unchecked)
- **Logging** — SLF4J + Logback instead of `System.out`
- **Testing** — JUnit 5, including regression tests for the two bugs described below
- **CI** — GitHub Actions runs `mvn verify` on every push and pull request

## 🛠 Tech Stack

Java 17 · Maven · JUnit 5 · SQLite (via `sqlite-jdbc`) · SLF4J + Logback · GitHub Actions

## 🚀 Getting Started

```bash
git clone https://github.com/Ajay9760/Library-Management-System.git
cd Library-Management-System
mvn test      # run the test suite
mvn compile exec:java -Dexec.mainClass=com.ajay.library.App   # run the demo (writes library.db)
```

## Design Decisions

**Interfaces over concrete repositories.** `BookRepository`/`PatronRepository` are
interfaces so `LibraryService` never knows whether it's talking to an in-memory map or a
real database — `App.java` wires up the SQLite implementations; the test suite wires up the
in-memory ones for speed and isolation.

**Checked vs. unchecked exceptions.** `LibraryException` (checked) models expected business
outcomes a caller should handle — `BookNotAvailableException`, `BorrowLimitExceededException`,
`BookNotBorrowedException`. `InvalidInputException` stays an unchecked `RuntimeException`
because it signals a contract violation (a null or unknown ID), not a legitimate business
outcome — callers shouldn't have to catch it on every call, any more than they catch `NullPointerException`.

**SQLite persistence scope.** `SqliteBookRepository` and `SqlitePatronRepository` persist
books and patrons as real rows. The active-loan relationship (which patron currently holds
which book, and since when) is deliberately **not** persisted yet — it still lives on the
in-memory `Patron.borrowedBooks` list, same as the original design. Persisting it properly
would mean a `borrow_records` table plus resolving each `Book` through a `BookRepository` on
load, which is a real enough change (not just a column) that I scoped it out rather than
half-wire it. Noting the gap here instead of hiding it.

## Known, fixed bugs (documented for context)

1. **A patron could "return" a book they never borrowed.** The original `Patron.returnBook`
   called `book.returnBook()` unconditionally, even when the patron had no matching loan
   record — so any patron could flip someone else's borrowed book back to "available" and
   silently corrupt the real borrower's state. Fixed by moving `book.returnBook()` inside the
   match branch and throwing `BookNotBorrowedException` otherwise. Covered by
   `anotherPatronCannotReturnSomeoneElsesBook` and `doubleReturnThrowsOnSecondAttempt`.
2. **No test for the fine boundary.** A book returned exactly on its due date should not be
   late. Covered by `fineIsZeroExactlyOnDueDate`.

## Running tests

```bash
mvn test
```
