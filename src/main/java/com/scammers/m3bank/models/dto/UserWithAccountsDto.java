package com.scammers.m3bank.models.dto;

import com.scammers.m3bank.enums.AccountType;
import com.scammers.m3bank.models.Account;
import com.scammers.m3bank.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserWithAccountsDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<AccountDto> accounts;

    public UserWithAccountsDto(User user, List<Account> accounts) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.accounts = accounts.stream().map(AccountDto::new).toList();
    }

    @Data
    public class AccountDto {
        private String accountUUID;
        private AccountType accountType;
        private Double balance;
        private boolean blocked;

        public AccountDto(Account account) {
            this.accountUUID = account.getAccountUUID();
            this.accountType = account.getAccountType();
            this.balance = account.getBalance();
            this.blocked = account.isBlocked();
        }
    }
}

