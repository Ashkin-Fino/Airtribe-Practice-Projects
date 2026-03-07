package com.airtribe.librarymanagementsystem.notification;

import com.airtribe.librarymanagementsystem.entity.Patron;

public interface NotificationStrategy {

    void sendNotification(Patron patron, String message);

}
