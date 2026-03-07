package com.airtribe.librarymanagementsystem.observer;

import com.airtribe.librarymanagementsystem.entity.Book;
import com.airtribe.librarymanagementsystem.entity.Patron;
import com.airtribe.librarymanagementsystem.notification.NotificationStrategy;

public class PatronObserver implements Observer {

    private Patron patron;
    private NotificationStrategy notificationStrategy;

    public PatronObserver(Patron patron,
                          NotificationStrategy notificationStrategy) {

        this.patron = patron;
        this.notificationStrategy = notificationStrategy;
    }

    @Override
    public void notify(Book book) {

        String message = "Reserved book available: " + book.getName();

        notificationStrategy.sendNotification(patron, message);

    }

    public Patron getPatron() {
        return patron;
    }
}
