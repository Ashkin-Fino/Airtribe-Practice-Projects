package com.airtribe.librarymanagementsystem.service;

import java.util.List;

import com.airtribe.librarymanagementsystem.entity.Book;
import com.airtribe.librarymanagementsystem.entity.Branch;
import com.airtribe.librarymanagementsystem.entity.InventoryBook;
import com.airtribe.librarymanagementsystem.enums.InventoryBookStatus;
import com.airtribe.librarymanagementsystem.repository.BranchRepository;

public class InventoryService {

    private BranchRepository branchRepository;

    public InventoryService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public void updateInventoryStatus(InventoryBook inventoryBook, InventoryBookStatus newStatus) {
        /*
            Updates the inventory status of a InventoryBook and its underlying Book object.
        */
        inventoryBook.setStatus(newStatus);
        Book book = inventoryBook.getBook();
        if (findAnyAvailableCopy(book) == null) {
            book.setAvailable(false);
        } else {
            book.setAvailable(true);
        }
    }

    public InventoryBook findAnyAvailableCopy(Book book) {
        /*
            Checks if a copy of a particular book is available in any branch.
            If a copy is available, it returns that copy. Otherwise returns null.
        */
        List<Branch> branches = branchRepository.getAllBranches();
        for (Branch branch : branches) {
            for (InventoryBook copy : branch.getStock()) {
                if (copy.getBook().equals(book) && copy.getStatus() == InventoryBookStatus.AVAILABLE) {
                    return copy;
                }
            }
        }
        return null;
    }

    public void generateInventoryReport(String branchId) {
        /*
            Generates the report for the given branch.
        */
        Branch branch = branchRepository.getBranchById(branchId);
    
        if (branch == null) {
            System.out.println("Branch not found.");
            return;
        }
    
        System.out.println("\n==============================================");
        System.out.println("Inventory Report for Branch: " + branch.getName());
        System.out.println("==============================================");
    
        List<InventoryBook> stock = branch.getStock();
    
        if (stock.isEmpty()) {
            System.out.println("No books in this branch.");
            return;
        }
    
        System.out.printf("%-15s %-25s %-15s %-12s %-10s\n",
                "InventoryID", "Book Name", "ISBN", "Status", "Location");
        System.out.println("--------------------------------------------------------------------------");
    
        for (InventoryBook inventoryBook : stock) {
            Book book = inventoryBook.getBook();
            System.out.printf("%-15s %-25s %-15s %-12s %-10s\n",
                    inventoryBook.getId(),
                    book.getName(),
                    book.getIsbn(),
                    inventoryBook.getStatus(),
                    inventoryBook.getLocation());
        }
        System.out.println("--------------------------------------------------------------------------");
    }

    public List<InventoryBook> findAllAvailableCopiesOf(Book book) {
        /*
            Finds all the copies of give Book across all branches and returns a list
            of InventoryBook objects representing the available copies.
        */
        List<Branch> branches = branchRepository.getAllBranches();
        List<InventoryBook> availableBooks = new java.util.ArrayList<>();
        for (Branch branch : branches) {
            for (InventoryBook copy : branch.getStock()) {
                if (copy.getBook().equals(book)) {
                    availableBooks.add(copy);
                }
            }
        }
        return availableBooks;
    }
}
