package com.airtribe.payflow.controller;

import com.airtribe.payflow.entity.Transaction;
import com.airtribe.payflow.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // Record a money transfer
    @PostMapping
    public Transaction sendMoney(@RequestBody @NonNull Transaction transaction) {
        return transactionService.sendMoney(transaction);
    }
}
