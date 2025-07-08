package com.scammers.m3bank.controllers;

import com.scammers.m3bank.enums.Role;
import com.scammers.m3bank.models.Account;
import com.scammers.m3bank.models.Transaction;
import com.scammers.m3bank.models.User;
import com.scammers.m3bank.models.dto.UserWithAccountsDto;
import com.scammers.m3bank.repositories.UserRepository;
import com.scammers.m3bank.services.AccountService;
import com.scammers.m3bank.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserRepository userRepository;
    private final AccountService accountService;
    private final TransactionService transactionService;

    @GetMapping("/users")
    public List<UserWithAccountsDto> getAllUsersWithAccounts() {
        List<User> users = userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.ROLE_USER)
                .toList();
        return users.stream().map(user -> {
            List<Account> accounts = accountService.getAccountsByUserId(user.getId());
            return new UserWithAccountsDto(user, accounts);
        }).toList();
    }

    @GetMapping("/users/{userId}/transactions")
    public List<Transaction> getUserTransactions(@PathVariable Long userId) {
        return transactionService.getTransactionsForUser(userId);
    }

    @PostMapping("/accounts/{accountUuid}/block")
    public ResponseEntity<Void> blockAccount(@PathVariable String accountUuid) {
        Account account = accountService.getAccountByUuid(accountUuid);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        account.setBlocked(true);
        accountService.save(account);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accounts/{accountUuid}/unblock")
    public ResponseEntity<Void> unblockAccount(@PathVariable String accountUuid) {
        Account account = accountService.getAccountByUuid(accountUuid);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        account.setBlocked(false);
        accountService.save(account);
        return ResponseEntity.ok().build();
    }
}

