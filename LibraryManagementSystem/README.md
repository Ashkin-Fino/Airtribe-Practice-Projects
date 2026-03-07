# Library Management System

## Overview
The Library Management System is a Java-based application designed to manage library operations such as book lending, returning, inventory management, and patron management. It supports both administrators and patrons with distinct functionalities.

## Features
- **Patron Features**:
  - Search for books by name or ISBN.
  - Borrow and return books.
  - Reserve books when unavailable.

- **Administrator Features**:
  - Add, restock, and discard books.
  - Create and close branches.
  - Generate inventory reports.
  - Migrate stock between branches.
  - Manage patrons.

## Class Diagram and Structure

 - com.airtribe.librarymanagementsystem
    |-entity
    |   |-Book
    |   |-InventoryBook
    |   |-Patron
    |   |-Reservation
    |   |-Branch
    |-enums
    |   |-InventoryBookStatus
    |   |-ReservatonStatus
    |-notification
    |   |-NotificationStrategy
    |   |-EmailNotification
    |   |-SMSNotification
    |-observer
    |   |-Observer
    |   |-PatronObserver
    |-repository
    |   |-BookRepository
    |   |-PatronRepository
    |   |-BranchRepository
    |   |-ReservationRepository
    |-service
    |   |-Searchable
    |   |-BookSearchService
    |   |-PatronSearchService
    |   |-LibraryService
    |   |-InventoryService
    |-Main


## How to Run
1. Clone the repository.
2. Open the project in your favorite Java IDE.
3. Run the `Main` class to start the application.

## Dependencies
- Java 8 or higher.
- No external libraries are required.

## Author
Rashk - Library Management System Project