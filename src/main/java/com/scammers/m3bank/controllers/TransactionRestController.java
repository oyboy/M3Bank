package com.scammers.m3bank.controllers;

import com.scammers.m3bank.models.Transaction;
import com.scammers.m3bank.models.User;
import com.scammers.m3bank.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/history")
public class TransactionRestController {
    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(@AuthenticationPrincipal User user) {
        List<Transaction> transactions = transactionService.getTransactionsForUser(user.getId());
        return ResponseEntity.ok(transactions);
    }
}
