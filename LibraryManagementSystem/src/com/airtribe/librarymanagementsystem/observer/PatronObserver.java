package com.airtribe.librarymanagementsystem.observer;

import com.airtribe.librarymanagementsystem.entity.Book;
import com.airtribe.librarymanagementsystem.entity.Patron;
import com.airtribe.librarymanagementsystem.notification.NotificationStrategy;

public class PatronObserver implements Observer {

    private final Patron patron;
    private final NotificationStrategy notificationStrategy;

    public PatronObserver(Patron patron, NotificationStrategy notificationStrategy) {
        this.patron = patron;
        this.notificationStrategy = notificationStrategy;
    }

    @Override
    public void notify(Book book) {
        /*
        Sends notification to patron through given strategy.
        */
        String message = "The reserved book: " + book.getName() + 
            "is available and has been reserved for you for 14 days.";
        notificationStrategy.sendNotification(patron, message);
    }

    public Patron getPatron() {
        /*
        Return the patron object.
        */
        return patron;
    }
}
