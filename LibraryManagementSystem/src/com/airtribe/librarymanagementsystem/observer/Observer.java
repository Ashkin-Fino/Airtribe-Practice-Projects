package com.airtribe.librarymanagementsystem.observer;

import com.airtribe.librarymanagementsystem.entity.Book;

public interface Observer {

    void notify(Book book);

}
