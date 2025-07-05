package com.scammers.m3bank.integration;

import com.scammers.m3bank.components.AccountRawMapper;
import com.scammers.m3bank.components.UserRawMapper;
import com.scammers.m3bank.enums.AccountType;
import com.scammers.m3bank.models.Account;
import com.scammers.m3bank.models.User;
import com.scammers.m3bank.repositories.AccountRepository;
import com.scammers.m3bank.repositories.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class AccountRepositoryTest extends AbstractTestContainer {
    private JdbcTemplate jdbcTemplate;
    private AccountRepository accountRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate = getJdbcTemplate();
        accountRepository = new AccountRepository(jdbcTemplate, new AccountRawMapper());
        userRepository = new UserRepository(jdbcTemplate, new UserRawMapper());

        User user = new User(1L, "John", "Doe", "john.doe@example.com", "password123");
        userRepository.save(user);
    }

    @Test
    void saveAccount() {
        Account account = new Account(1L, AccountType.DEBIT, 100.0);
        accountRepository.save(account);

        List<Account> accountsAfter = jdbcTemplate.query(
                "SELECT * FROM accounts WHERE account_uuid = ?", new AccountRawMapper(), account.getAccountUUID());

        assertThat(accountsAfter.size()).isEqualTo(1);
        assertThat(accountsAfter.get(0).getAccountUUID()).isEqualTo(account.getAccountUUID());
        assertThat(accountsAfter.get(0).getBalance()).isEqualTo(100.0);
    }

    @Test
    void saveAccountShouldUpdateIfExists() {
        Account account = new Account(1L, AccountType.DEBIT, 100.0);
        accountRepository.save(account);

        account.setBalance(200.0);
        accountRepository.save(account);

        List<Account> accountsAfter = jdbcTemplate.query(
                "SELECT * FROM accounts WHERE account_uuid = ?", new AccountRawMapper(), account.getAccountUUID());

        assertThat(accountsAfter.size()).isEqualTo(1);
        assertThat(accountsAfter.get(0).getBalance()).isEqualTo(200.0);
    }

    @Test
    void findByUuid() {
        Account account = new Account(1L, AccountType.DEBIT, 100.0);
        accountRepository.save(account);

        Account foundAccount = accountRepository.findByUuid(account.getAccountUUID());
        assertNotNull(foundAccount);
        assertThat(foundAccount.getAccountUUID()).isEqualTo(account.getAccountUUID());
        assertThat(foundAccount.getBalance()).isEqualTo(100.0);
    }

    @Test
    void willReturnNullWhenUuidDoesNotExist() {
        Account account = accountRepository.findByUuid("nonexistent-uuid");
        assertNull(account);
    }

    @Test
    void findAllByUserId() {
        Account account1 = new Account(1L, AccountType.DEBIT, 100.0);
        Account account2 = new Account(1L, AccountType.CREDIT, 200.0);
        accountRepository.save(account1);
        accountRepository.save(account2);

        List<Account> accounts = accountRepository.findAllByUserId(1L);
        assertThat(accounts.size()).isEqualTo(2);
        assertThat(accounts.get(0).getUserId()).isEqualTo(1L);
        assertThat(accounts.get(1).getUserId()).isEqualTo(1L);
    }

    @Test
    void deleteByUuid() {
        Account account = new Account(1L, AccountType.DEBIT, 100.0);
        accountRepository.save(account);

        accountRepository.delete(account.getAccountUUID());

        Account deletedAccount = accountRepository.findByUuid(account.getAccountUUID());
        assertNull(deletedAccount);
    }

    @Test
    void deleteById() {
        Account account = new Account(1L, AccountType.DEBIT, 100.0);
        accountRepository.save(account);

        Long accountId = jdbcTemplate.queryForObject(
                "SELECT id FROM accounts WHERE account_uuid = ?", Long.class, account.getAccountUUID());

        accountRepository.delete(accountId);

        Account deletedAccount = accountRepository.findByUuid(account.getAccountUUID());
        assertNull(deletedAccount);
    }

    @Test
    void updateBalanceByUuid() {
        Account account = new Account(1L, AccountType.DEBIT, 100.0);
        accountRepository.save(account);

        accountRepository.updateBalance(account.getAccountUUID(), 250.0);

        Account updatedAccount = accountRepository.findByUuid(account.getAccountUUID());
        assertThat(updatedAccount.getBalance()).isEqualTo(250.0);
    }

    @Test
    void updateBalanceById() {
        Account account = new Account(1L, AccountType.DEBIT, 100.0);
        accountRepository.save(account);

        Long accountId = jdbcTemplate.queryForObject(
                "SELECT id FROM accounts WHERE account_uuid = ?", Long.class, account.getAccountUUID());

        accountRepository.updateBalance(accountId, 300.0);

        Account updatedAccount = accountRepository.findByUuid(account.getAccountUUID());
        assertThat(updatedAccount.getBalance()).isEqualTo(300.0);
    }

    @Test
    void getUserIdByUuid() {
        Account account = new Account(1L, AccountType.DEBIT, 100.0);
        accountRepository.save(account);

        Long userId = accountRepository.getUserId(account.getAccountUUID());
        assertThat(userId).isEqualTo(1L);
    }
}