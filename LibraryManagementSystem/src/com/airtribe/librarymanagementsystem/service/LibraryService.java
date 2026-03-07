package com.airtribe.librarymanagementsystem.service;

import java.time.LocalDate;
import java.util.List;

import com.airtribe.librarymanagementsystem.entity.Book;
import com.airtribe.librarymanagementsystem.entity.Branch;
import com.airtribe.librarymanagementsystem.entity.InventoryBook;
import com.airtribe.librarymanagementsystem.entity.Patron;
import com.airtribe.librarymanagementsystem.entity.Receipt;
import com.airtribe.librarymanagementsystem.enums.InventoryBookStatus;
import com.airtribe.librarymanagementsystem.notification.NotificationStrategy;
import com.airtribe.librarymanagementsystem.observer.PatronObserver;
import com.airtribe.librarymanagementsystem.repository.BookRepository;
import com.airtribe.librarymanagementsystem.repository.BranchRepository;
import com.airtribe.librarymanagementsystem.repository.ReceiptRepository;

public class LibraryService {

    private BookRepository bookRepository;
    private BranchRepository branchRepository;
    private ReceiptRepository receiptRepository;
    private InventoryService inventoryService;

    public LibraryService(BookRepository bookRepository,
                          BranchRepository branchRepository,
                          ReceiptRepository receiptRepository,
                          InventoryService inventoryService) {

        this.bookRepository = bookRepository;
        this.branchRepository = branchRepository;
        this.receiptRepository = receiptRepository;
        this.inventoryService = inventoryService;
    }

    public Receipt lendBook(String isbn, Patron patron) {

        if (!patron.canBorrow()) {
            throw new RuntimeException("Patron borrow limit reached");
        }

        Book book = bookRepository.getBookByIsbn(isbn);

        if (book == null) {
            throw new RuntimeException("Book not found");
        }

        InventoryBook availableCopy = findAvailableCopy(book);

        if (availableCopy == null) {
            throw new RuntimeException("Book not available");
        }

        inventoryService.updateInventoryStatus(
                availableCopy,
                InventoryBookStatus.LENT
        );

        patron.incrementBorrowedBooks();

        LocalDate lendDate = LocalDate.now();
        LocalDate dueDate = lendDate.plusDays(14);

        Receipt receipt =
                new Receipt(availableCopy, lendDate, dueDate, patron);

        receiptRepository.addReceipt(receipt);

        return receipt;
    }

    public void reserveBook(String isbn,
                            Patron patron,
                            NotificationStrategy strategy) {

        Book book = bookRepository.getBookByIsbn(isbn);

        if (book == null) {
            throw new RuntimeException("Book not found");
        }

        PatronObserver observer =
                new PatronObserver(patron, strategy);

        book.addReservation(observer);
    }

    public void returnBook(InventoryBook inventoryBook) {

        Book book = inventoryBook.getBook();

        if (book.hasReservations()) {

            PatronObserver nextObserver = book.getNextReservation();

            nextObserver.notify(book);

            inventoryBook.setStatus(InventoryBookStatus.RESERVED);

        } else {

            inventoryService.updateInventoryStatus(
                    inventoryBook,
                    InventoryBookStatus.AVAILABLE
            );

        }

    }

    private InventoryBook findAvailableCopy(Book book) {

        List<Branch> branches = branchRepository.getAllBranches();

        for (Branch branch : branches) {

            for (InventoryBook copy : branch.getStock()) {

                if (copy.getBook().equals(book) &&
                        copy.getStatus() == InventoryBookStatus.AVAILABLE) {

                    return copy;
                }
            }
        }
        return null;
    }
}
