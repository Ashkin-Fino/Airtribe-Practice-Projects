package com.airtribe.librarymanagementsystem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.airtribe.librarymanagementsystem.entity.Book;
import com.airtribe.librarymanagementsystem.repository.BookRepository;

public class BookSearchService implements Searchable<Book> {

    private BookRepository bookRepository;
    private Scanner scanner;

    public BookSearchService(BookRepository bookRepository, Scanner scanner) {
        this.bookRepository = bookRepository;
        this.scanner = scanner;
    }

    @Override
    public Book search() {
        /*
        Asks user whether to search by name or id. Gets the input from user and
        then calls the respective search method.
        */
       
        int retryCount = 0;
        while(true) {
            System.out.println("How do you want to search Book?");
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
                        System.out.println("You entered wrong choise 3 times. Returning to menu.");
                        return null;
                    }
                    System.out.println("Invalid choice. Try again");
                    retryCount++;
            }
        }
    }

    public Book searchById(String id) {
        /*
        Gets id as input from user and searches the Book with the same id.
        */

        Book book = bookRepository.getBookByIsbn(id);

        if (book == null) {
            System.out.println("No book found with the id: " + id);
            return null;
        }
        return book;
    }

    public Book searchByName(String name) {
        /*
        Gets name as input from user and searches the Book with the same name.
        If multiple matches found, asks user to choose 1 and returns it.
        */
        List<Book> matches = new ArrayList<>();

        for (Book book : bookRepository.getAllBooks()) {
            if (book.getName().toLowerCase().contains(name.toLowerCase())) {
                matches.add(book);
            }
        }

        if (matches.isEmpty()) {
            System.out.println("No books found with the name: " + name);
            return null;
        }
        if (matches.size() == 1) {
            return matches.get(0);
        }

        System.out.println("Multiple books found:");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println((i + 1) + ". " + matches.get(i));
        }

        System.out.println("Choose a book:");
        int choice = scanner.nextInt();
        if (choice < 1 || choice > matches.size()) {
            System.out.println("Invalid choice.");
            return null;
        }
        return matches.get(choice - 1);
    }
}