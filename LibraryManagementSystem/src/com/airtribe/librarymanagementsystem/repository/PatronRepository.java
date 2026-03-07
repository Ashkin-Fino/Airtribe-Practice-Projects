package com.airtribe.librarymanagementsystem.repository;

import java.util.*;

import com.airtribe.librarymanagementsystem.entity.Patron;

public class PatronRepository {

    private Map<String, Patron> patrons = new HashMap<>();

    public void addPatron(Patron patron) {
        patrons.put(patron.getId(), patron);
    }

    public Patron getPatronById(String id) {
        return patrons.get(id);
    }

    public List<Patron> getAllPatrons() {
        return new ArrayList<>(patrons.values());
    }

    public void removePatron(String id) {
        patrons.remove(id);
    }

}
