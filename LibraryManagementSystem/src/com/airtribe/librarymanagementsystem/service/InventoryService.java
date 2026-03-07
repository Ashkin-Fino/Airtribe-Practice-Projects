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

    public void updateInventoryStatus(InventoryBook inventoryBook,
                                      InventoryBookStatus newStatus) {

        inventoryBook.setStatus(newStatus);

        Book book = inventoryBook.getBook();

        boolean available = checkAnyAvailableCopy(book);

        book.setAvailable(available);
    }

    private boolean checkAnyAvailableCopy(Book book) {

        List<Branch> branches = branchRepository.getAllBranches();

        for (Branch branch : branches) {

            for (InventoryBook copy : branch.getStock()) {

                if (copy.getBook().equals(book) &&
                        copy.getStatus() == InventoryBookStatus.AVAILABLE) {

                    return true;
                }

            }
        }

        return false;
    }

}