package com.scammers.m3bank.models;

import com.scammers.m3bank.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Account {
    private Long id;
    private final String accountUUID;
    private Double balance;
    private Long userId;
    private AccountType accountType;
    private boolean blocked;

    public Account(){
        this.accountUUID = UUID.randomUUID().toString();
    }
    public Account(Long userId, AccountType accountType, Double balance) {
        this.accountUUID = UUID.randomUUID().toString();
        this.accountType = accountType;
        this.userId = userId;
        this.balance = balance;
    }
}
