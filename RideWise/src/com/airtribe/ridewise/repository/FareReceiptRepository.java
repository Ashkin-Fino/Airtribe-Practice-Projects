package com.airtribe.ridewise.repository;

import java.util.*;

import com.airtribe.ridewise.entity.FareReceipt;

public class FareReceiptRepository {
    private Map<String, FareReceipt> receipts = new HashMap<>();

    public void save(FareReceipt receipt) {
        receipts.put(receipt.getId(), receipt);
    }
}
