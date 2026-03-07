package com.airtribe.librarymanagementsystem.repository;

import java.util.*;

import com.airtribe.librarymanagementsystem.entity.Patron;
import com.airtribe.librarymanagementsystem.entity.Receipt;

public class ReceiptRepository {

    private List<Receipt> receipts = new ArrayList<>();

    public void addReceipt(Receipt receipt) {
        receipts.add(receipt);
    }

    public List<Receipt> getAllReceipts() {
        return new ArrayList<>(receipts);
    }

    public List<Receipt> getReceiptsByPatron(Patron patron) {

        List<Receipt> result = new ArrayList<>();

        for (Receipt receipt : receipts) {
            if (receipt.getPatron().equals(patron)) {
                result.add(receipt);
            }
        }

        return result;
    }

}
