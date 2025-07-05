package com.scammers.m3bank.services;

import com.scammers.m3bank.models.Transaction;
import com.scammers.m3bank.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsForUser(long userId) {
        return transactionRepository.getTransactionsByUserId(userId);
    }
}
