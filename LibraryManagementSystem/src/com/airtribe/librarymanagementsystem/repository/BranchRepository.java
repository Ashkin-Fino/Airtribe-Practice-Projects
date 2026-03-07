package com.airtribe.librarymanagementsystem.repository;

import java.util.*;

import com.airtribe.librarymanagementsystem.entity.Branch;
import com.airtribe.librarymanagementsystem.entity.InventoryBook;

public class BranchRepository {

    private Map<String, Branch> branches = new HashMap<>();

    public void addBranch(Branch branch) {
        branches.put(branch.getId(), branch);
    }

    public Branch getBranchById(String id) {
        return branches.get(id);
    }

    public List<Branch> getAllBranches() {
        return new ArrayList<>(branches.values());
    }

    public void removeBranch(String id) {
        branches.remove(id);
    }

    public InventoryBook getInventoryBookById(String inventoryBookId) {
        for (Branch branch : branches.values()) {
            for (InventoryBook inventoryBook : branch.getStock()) {
                if (inventoryBook.getId().equals(inventoryBookId)) {
                    return inventoryBook;
                }
            }
        }
        return null;
    }
}
