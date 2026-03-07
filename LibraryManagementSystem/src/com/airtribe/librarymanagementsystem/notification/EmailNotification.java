package com.airtribe.librarymanagementsystem.notification;

import com.airtribe.librarymanagementsystem.entity.Patron;

public class EmailNotification implements NotificationStrategy {

    @Override
    public void sendNotification(Patron patron, String message) {

        System.out.println(
                "Sending EMAIL to " + patron.getEmail()
                + " : " + message
        );

    }

}
