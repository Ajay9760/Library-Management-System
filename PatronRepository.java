package src.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import src.model.Patron;

/**
 * Repository for managing patrons.
 */
public class PatronRepository {

    private Map<String, Patron> patrons = new HashMap<>();

    /**
     * Adds a patron to the repository.
     * @param patron the patron to add
     */
    public void addPatron(Patron patron) {
        patrons.put(patron.getId(), patron);
    }

    /**
     * Retrieves a patron by ID.
     * @param id the ID of the patron to retrieve
     * @return the patron, or an empty Optional if not found
     */
    public Optional<Patron> getPatronById(String id) {
        return Optional.ofNullable(patrons.get(id));
    }

    public Patron[] getAllPatrons() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'getAllPatrons'");
    }
}