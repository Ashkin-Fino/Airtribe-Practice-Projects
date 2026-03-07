package com.airtribe.librarymanagementsystem.repository;

import java.util.*;

import com.airtribe.librarymanagementsystem.entity.Book;

public class BookRepository {

    private Map<String, Book> books = new HashMap<>();

    public void addBook(Book book) {
        books.put(book.getIsbn(), book);
    }

    public Book getBookByIsbn(String isbn) {
        return books.get(isbn);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public void removeBook(String isbn) {
        books.remove(isbn);
    }

}
