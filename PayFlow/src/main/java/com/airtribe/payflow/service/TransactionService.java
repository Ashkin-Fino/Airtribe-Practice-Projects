package com.airtribe.payflow.service;

import com.airtribe.payflow.entity.Transaction;
import com.airtribe.payflow.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {


    @Autowired
    private TransactionRepository transactionRepository;

    /*
     Spring creates the TransactionRepository implementation bean automatically
     during startup because it extends JpaRepository.

     Using @Autowired, Spring injects this repository bean into this service,
     allowing the service layer to communicate with the database.
    */

    // Save transaction record
    // Balance deduction logic will be added later
    public Transaction sendMoney(@NonNull Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
