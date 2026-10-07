package com.ajay.library.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ajay.library.model.Book;
import com.ajay.library.model.BookCategory;

/**
 * SQLite-backed {@link BookRepository}. Persists id, title, author, category,
 * and availability to a real table via plain JDBC (PreparedStatements only -
 * no string-concatenated SQL).
 *
 * Scope note: only the Book entity itself is persisted here. Which books a
 * given patron currently has on loan (the Patron/BorrowRecord relationship)
 * still lives in memory on the Patron aggregate for this iteration - see
 * README "Design Decisions" for why, and what persisting it fully would add.
 *
 * Opens a short-lived connection per call rather than pooling one, which is
 * the right tradeoff at this scale (a portfolio project, not a
 * high-throughput service) and keeps lifecycle management simple.
 */
public class SqliteBookRepository implements BookRepository {
    private final String jdbcUrl;

    public SqliteBookRepository(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        createSchema();
    }

    private void createSchema() {
        String ddl = "CREATE TABLE IF NOT EXISTS books (" +
            "id TEXT PRIMARY KEY, " +
            "title TEXT NOT NULL, " +
            "author TEXT NOT NULL, " +
            "category TEXT NOT NULL, " +
            "is_available INTEGER NOT NULL)";
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             Statement stmt = conn.createStatement()) {
            stmt.execute(ddl);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to initialize books table", e);
        }
    }

    @Override
    public Book save(Book book) {
        String sql = "INSERT INTO books (id, title, author, category, is_available) VALUES (?, ?, ?, ?, ?) " +
            "ON CONFLICT(id) DO UPDATE SET title = excluded.title, author = excluded.author, " +
            "category = excluded.category, is_available = excluded.is_available";
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getId());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setString(4, book.getCategory().name());
            ps.setInt(5, book.isAvailable() ? 1 : 0);
            ps.executeUpdate();
            return book;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save book: " + book.getId(), e);
        }
    }

    @Override
    public Optional<Book> findById(String id) {
        String sql = "SELECT id, title, author, category, is_available FROM books WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find book: " + id, e);
        }
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT id, title, author, category, is_available FROM books";
        List<Book> result = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.add(mapRow(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to list books", e);
        }
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to delete book: " + id, e);
        }
    }

    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM books");
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to clear books table", e);
        }
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        Book book = new Book(
            rs.getString("id"),
            rs.getString("title"),
            rs.getString("author"),
            BookCategory.valueOf(rs.getString("category"))
        );
        book.setAvailable(rs.getInt("is_available") == 1);
        return book;
    }
}
