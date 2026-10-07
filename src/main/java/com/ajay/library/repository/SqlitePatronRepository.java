package com.ajay.library.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ajay.library.model.Patron;

/**
 * SQLite-backed {@link PatronRepository}. Persists id and name.
 *
 * Scope note: a Patron's active loans (borrowedBooks) are NOT reconstructed
 * from the database - findById/getAllPatrons return a Patron with an empty
 * loan list, matching what InMemoryPatronRepository would give you after a
 * restart anyway. Persisting the loan relationship would mean a third
 * borrow_records table (book_id, patron_id, borrow_date, due_date) plus
 * resolving each Book through a BookRepository on load; left out here to
 * keep this iteration's scope honest rather than half-wire it. See README
 * "Design Decisions".
 */
public class SqlitePatronRepository implements PatronRepository {
    private final String jdbcUrl;

    public SqlitePatronRepository(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        createSchema();
    }

    private void createSchema() {
        String ddl = "CREATE TABLE IF NOT EXISTS patrons (" +
            "id TEXT PRIMARY KEY, " +
            "name TEXT NOT NULL)";
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             Statement stmt = conn.createStatement()) {
            stmt.execute(ddl);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to initialize patrons table", e);
        }
    }

    @Override
    public Patron save(Patron patron) {
        String sql = "INSERT INTO patrons (id, name) VALUES (?, ?) " +
            "ON CONFLICT(id) DO UPDATE SET name = excluded.name";
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, patron.getId());
            ps.setString(2, patron.getName());
            ps.executeUpdate();
            return patron;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save patron: " + patron.getId(), e);
        }
    }

    @Override
    public Optional<Patron> findById(String id) {
        String sql = "SELECT id, name FROM patrons WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Patron(rs.getString("id"), rs.getString("name")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find patron: " + id, e);
        }
    }

    @Override
    public List<Patron> getAllPatrons() {
        String sql = "SELECT id, name FROM patrons";
        List<Patron> result = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.add(new Patron(rs.getString("id"), rs.getString("name")));
            }
            return result;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to list patrons", e);
        }
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM patrons WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to delete patron: " + id, e);
        }
    }

    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM patrons");
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to clear patrons table", e);
        }
    }
}
