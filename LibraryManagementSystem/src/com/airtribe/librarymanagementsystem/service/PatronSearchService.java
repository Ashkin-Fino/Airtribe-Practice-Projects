package com.airtribe.librarymanagementsystem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.airtribe.librarymanagementsystem.entity.Patron;
import com.airtribe.librarymanagementsystem.repository.PatronRepository;

public class PatronSearchService implements Searchable<Patron> {

    private PatronRepository patronRepository;
    private Scanner scanner;

    public PatronSearchService(PatronRepository patronRepository, Scanner scanner) {

        this.patronRepository = patronRepository;
        this.scanner = scanner;
    }

    @Override
    public Patron search() {
        /*
        Asks user whether to search by name or id. Gets the input from user and
        then calls the respective search method.
        */

        int retryCount = 0;
        while(true) {
            System.out.println("How do you want to search Patron?");
            System.out.println("1. Search using Id");
            System.out.println("2. Search using Name");
            System.out.println("3. Back");
            int role = scanner.nextInt();
            switch (role) {
                case 1:
                    System.out.println("Enter Id:");
                    String id = scanner.next().toString();
                    return searchById(id);
                case 2:
                    System.out.println("Enter Name:");
                    String name = scanner.next().toString();
                    return searchByName(name);
                case 3:
                    return null;
                default:
                    if (retryCount>3) {
                        System.out.println("You entered wrong choice 3 times.");
                        return null;
                    }
                    System.out.println("Invalid choice. Try again");
                    retryCount++;
            }
        }
    }

    public Patron searchById(String id) {
        /*
        Gets id as input from user and searches the Book with the same id.
        */
       
        Patron patron = patronRepository.getPatronById(id);
        if (patron == null) {
            System.out.println("No patron found with the given id.");
            return null;
        }
        return patron;
    }

    public Patron searchByName(String name) {
        /*
        Gets name as input from user and searches the Patron with the same name.
        If multiple matches found, asks user to choose 1 and returns it.
        */
        List<Patron> matches = new ArrayList<>();

        for (Patron patron : patronRepository.getAllPatrons()) {
            if (patron.getName().equalsIgnoreCase(name)) {
                matches.add(patron);
            }
        }

        if (matches.isEmpty()) {
            System.out.println("No patron found with the given name.");
            return null;
        }
        if (matches.size() == 1) {
            return matches.get(0);
        }

        System.out.println("Multiple patrons found:");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println((i + 1) + ". " + matches.get(i));
        }

        System.out.println("Choose a patron:");
        int choice = scanner.nextInt();
        if (choice < 1 || choice > matches.size()) {
            System.out.println("Invalid choice.");
            return null;
        }

        return matches.get(choice - 1);
    }
}
