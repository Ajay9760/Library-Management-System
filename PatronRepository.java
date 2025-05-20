package src.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import src.model.Patron;

public class PatronRepository {

    private Map<String, Patron> patrons = new HashMap<>();

    public void addPatron(Patron patron) {
        patrons.put(patron.getId(), patron);
    }

    public Optional<Patron> getPatronById(String id) {
        return Optional.ofNullable(patrons.get(id));
    }

    public Patron[] getAllPatrons() {
      throw new UnsupportedOperationException("Unimplemented method 'getAllPatrons'");
    }
}
