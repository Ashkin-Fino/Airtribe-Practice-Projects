package com.airtribe.librarymanagementsystem.notification;

import com.airtribe.librarymanagementsystem.entity.Patron;

public class SMSNotification implements NotificationStrategy {

    @Override
    public void sendNotification(Patron patron, String message) {

        System.out.println(
                "Sending SMS to " + patron.getName()
                + " : " + message
        );

    }

}
