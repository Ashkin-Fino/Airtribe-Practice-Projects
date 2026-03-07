package com.airtribe.librarymanagementsystem.service;

import java.util.List;
import java.util.Scanner;

import com.airtribe.librarymanagementsystem.entity.Book;
import com.airtribe.librarymanagementsystem.entity.Branch;
import com.airtribe.librarymanagementsystem.entity.InventoryBook;
import com.airtribe.librarymanagementsystem.entity.Patron;
import com.airtribe.librarymanagementsystem.entity.Reservation;
import com.airtribe.librarymanagementsystem.enums.InventoryBookStatus;
import com.airtribe.librarymanagementsystem.enums.ReservationStatus;
import com.airtribe.librarymanagementsystem.notification.EmailNotification;
import com.airtribe.librarymanagementsystem.notification.NotificationStrategy;
import com.airtribe.librarymanagementsystem.observer.PatronObserver;
import com.airtribe.librarymanagementsystem.repository.BookRepository;
import com.airtribe.librarymanagementsystem.repository.BranchRepository;
import com.airtribe.librarymanagementsystem.repository.PatronRepository;
import com.airtribe.librarymanagementsystem.repository.ReservationRepository;

public class LibraryService {

    private BookRepository bookRepository;
    private BranchRepository branchRepository;
    private ReservationRepository reservationRepository;
    private InventoryService inventoryService;
    private Searchable<Book> bookSearchService;
    private PatronRepository patronRepository;
    private Scanner scanner;

    public LibraryService(BookRepository bookRepository, BranchRepository branchRepository,
                    ReservationRepository reservationRepository, InventoryService inventoryService,
                    PatronRepository pr, Searchable<Book> bookSearchService, Scanner scanner) {
        /*
        Parameterized constructor with BookRepository, BranchRepository, ReservationRepository,
        InventoryService, PatronSearchService and BookSearchService
        */
        this.bookRepository = bookRepository;
        this.branchRepository = branchRepository;
        this.reservationRepository = reservationRepository;
        this.patronRepository = pr;
        this.inventoryService = inventoryService;
        this.bookSearchService = bookSearchService;
        this.scanner = scanner;
    }

    public Reservation lendBook(Patron patron) {
        /*
            This method handles the process of lending a book to a patron. It first checks if
            the patron can borrow more books (not exceeding the limit of 5). Then it searches for
            a book using the book search service. If a book is found, it checks for an available copy 
            in the inventory. If an available copy is found, it updates the inventory status to LENT, 
            increments the patron's borrowed books count, creates a reservation and adds it
            to the reservation repository. If no available copy is found, it offers the patron the
            option to reserve the book. If the patron chooses to reserve, it calls the reserveBook method.
            Returns the created reservation.
        */

        if (!patron.canBorrow()) {
            throw new RuntimeException("Patron borrow limit reached");
        }
        System.out.println("Searching book to borrow...");
        Book book = this.bookSearchService.search();

        if (book == null) {
            throw new RuntimeException("Book not found");
        }

        InventoryBook availableCopy = inventoryService.findAnyAvailableCopy(book);

        if (availableCopy == null) {
            System.out.println("No available copies. Do you want to reserve the book?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            int choice = scanner.nextInt();
            if(choice == 1) {
                preReserveBook(book, patron, new EmailNotification());
            }
        }

        System.out.println("How many days do you want to borrow the book for? (Max: 30 Days)");
        int days = scanner.nextInt();
        if(days > 30 || days <= 0) {
            System.out.println("Invalid number of days. Setting to default 14 days.");
            days = 14;
        }

        return reserveBook(availableCopy, patron, days);
    }

    public void preReserveBook(Book book, Patron patron, NotificationStrategy strategy) {
        /*
            This method handles the process of reserving a book for a patron. It creates 
            a PatronObserver with the given patron and notification strategy, and adds it 
            to the book's reservation list. When the book becomes available, the observer 
            will be notified and the patron will receive a notification according to the 
            specified strategy.
        */
        PatronObserver observer = new PatronObserver(patron, strategy);
        book.addReservation(observer);
    }

    private Reservation reserveBook(InventoryBook book, Patron patron, int days) {
        /*
            This method creates a reservation for the given book and patron. It updates 
            the inventory status to LENT, increments the patron's borrowed books 
            count, creates a reservation object and adds it to the reservation repository. 
            Returns the created reservation.
        */
        inventoryService.updateInventoryStatus(book, InventoryBookStatus.LENT);
        patron.setBorrowedBooksCount(patron.getBorrowedBooksCount() + 1);
        Reservation reservation = new Reservation(book, days, patron);
        reservationRepository.addReservation(reservation);

        return reservation;
    }

    public void returnBook(Patron patron) {
        /*
            This method handles the process of returning a book by a patron. It retrieves the list of 
            reservations for the patron and displays the books that are currently borrowed (in progress). 
            The patron can choose which book to return. Once a book is selected, it updates the inventory 
            status to AVAILABLE, decrements the patron's borrowed books count, and checks if there are 
            any reservations for that book. If there are reservations, it gets the next observer in line, 
            reserves the book for that observer's patron, and notifies them about the availability of the book.
        */
        
        List<Reservation> reservations = reservationRepository.getReservationsByPatron(patron);
        if (reservations.isEmpty()) {
            System.out.println("No borrowed books found for this patron.");
            return;
        }
        System.out.println("Choose the book you want to return:");
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getstatus() == ReservationStatus.IN_PROGRESS) {
                System.out.println((i + 1) + ". " + reservations.get(i).getBook().getBook().getName());
            }
        }
        int choice = scanner.nextInt();
        if (choice < 1 || choice > reservations.size()) {
            System.out.println("Invalid choice. Returning to patron menu.");
            return;
        }
        InventoryBook inventoryBook = reservations.get(choice - 1).getBook();
        Book book = inventoryBook.getBook();
        inventoryService.updateInventoryStatus(inventoryBook,InventoryBookStatus.AVAILABLE);

        if (book.hasReservations()) {
            PatronObserver nextObserver = book.getNextReservation();
            reserveBook(inventoryBook, nextObserver.getPatron(), 14);
            nextObserver.notify(book);
        }
    }

    public void addNewBook() {
        /*
            This method handles the process of adding a new book to the library. It prompts the 
            administrator for book details, creates a new book object, and adds it to the book repository.
        */
        System.out.println("Enter book name:");
        String name = scanner.next();
        System.out.println("Enter author name:");
        String author = scanner.next();
        System.out.println("Enter ISBN:");
        String isbn = scanner.next();
        System.out.println("Enter published year:");
        int year = scanner.nextInt();
        Book book = new Book(name, author, year, isbn);
        bookRepository.addBook(book);
        System.out.println("Book added successfully.");
    }

    public void restockBook() {
        /*
            This method handles the process of restocking books in the inventory. It prompts the 
            administrator for the book details and the branch where the stock should be added, 
            and updates the inventory.
        */
        Book book = bookSearchService.search();
        if (book == null) {
            System.out.println("Returning to Main menu");
            return;
        }
        System.out.println("Enter branch ID:");
        String branchId = scanner.next();
        Branch branch = branchRepository.getBranchById(branchId);
        if (branch == null) {
            System.out.println("Branch not found. Returning to Main menu.");
            return;
        }
        System.out.println("Enter quantity to restock:");
        int quantity = scanner.nextInt();
        for (int i=0; i<quantity; i++) {
            InventoryBook inventoryBook = new InventoryBook(book);
            branch.restockBook(inventoryBook);
        }
        book.addStock(quantity);
        System.out.println("Books restocked successfully.");
    }

    public void discardBook() {
        /*
            This method handles the process of discarding books from the inventory. It prompts the 
            administrator for the book details and the branch where the stock should be discarded, 
            and updates the inventory accordingly.
        */
        Book book = bookSearchService.search();
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        System.out.println("Enter branch ID:");
        String branchId = scanner.next();
        Branch branch = branchRepository.getBranchById(branchId);
        if (branch == null) {
            System.out.println("Branch not found.");
            return;
        }

        List<InventoryBook> stocksToDiscard = branch.getStock().stream().filter(inventoryBook -> inventoryBook.getBook()
                        .equals(book) && inventoryBook.getStatus() == InventoryBookStatus.AVAILABLE).toList();
        if (stocksToDiscard.isEmpty()) {
            System.out.println("No available copies of this book in the selected branch.");
            return;
        }

        System.out.println("Choose book to discard:");
        for (int i = 0; i < stocksToDiscard.size(); i++) {
            System.out.println((i + 1) + ". " + stocksToDiscard.get(i).getBook().getName());
        }
        int choice = scanner.nextInt();
        if (choice < 1 || choice > stocksToDiscard.size()) {
            System.out.println("Invalid choice. Returning to admin menu.");
            return;
        }
        InventoryBook inventoryBook = stocksToDiscard.get(choice - 1);
        branch.discardBook(inventoryBook);
        book.removeStock(1);
    }

    public void createBranch() {
        /*
            This method handles the process of creating a new branch. It prompts the administrator 
            for branch details, creates a new branch object, and adds it to the branch repository.
        */
        System.out.println("Enter branch name:");
        String name = scanner.next();
        System.out.println("Enter branch location:");
        String location = scanner.next();
        Branch branch = new Branch(name, location);
        branchRepository.addBranch(branch);
        System.out.println("Branch created successfully: " + branch.toString());
    }

    public void closeBranch() {
        /*
            to be implemented.
        */
    }

    public void migrateStocks() {
        /*
            to be implemented
        */
    }

    public void createPatron() {
        /*
            This method handles the process of creating a new patron. It prompts the administrator 
            for patron details, creates a new patron object, and displays the created 
            patron information.
        */
        System.out.println("Enter patron name:");
        String name = scanner.next();
        System.out.println("Enter patron email:");
        String email = scanner.next();
        System.out.println("Enter patron address:");
        String address = scanner.next();
        System.out.println("Enter patron age:");
        int age = scanner.nextInt();
        Patron patron = new Patron(name, address, email, age);

        System.out.println("Patron created successfully: " + patron.toString());

        patronRepository.addPatron(patron);
    }
}
