package com.scammers.m3bank.integration;

import com.scammers.m3bank.components.AccountRawMapper;
import com.scammers.m3bank.components.TransactionRawMapper;
import com.scammers.m3bank.enums.AccountType;
import com.scammers.m3bank.enums.TransactionType;
import com.scammers.m3bank.models.Account;
import com.scammers.m3bank.models.Transaction;
import com.scammers.m3bank.repositories.AccountRepository;
import com.scammers.m3bank.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.util.List;


public class TransactionRepositoryTest extends AbstractTestContainer {
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate = getJdbcTemplate();
        accountRepository = new AccountRepository(jdbcTemplate, new AccountRawMapper());
        transactionRepository = new TransactionRepository(jdbcTemplate, new TransactionRawMapper());

        jdbcTemplate.update("INSERT INTO users (id, first_name, last_name, email, password) VALUES (?, ?, ?, ?, ?)", 1L, "John", "Doe", "john.doe@example.com", "pass");
        jdbcTemplate.update("INSERT INTO users (id, first_name, last_name, email, password) VALUES (?, ?, ?, ?, ?)", 2L, "John2", "Doe", "john2.doe@example.com", "pass2");
    }

    @Test
    void saveTransaction() {
        Transaction transaction = Transaction.builder()
                .id(1L)
                .type(TransactionType.DEPOSIT)
                .amount(1000.00)
                .timestamp(LocalDateTime.now())
                .sourceAccountId("uuid123")
                .build();

        transactionRepository.save(transaction);

        Transaction fetchedTransaction = transactionRepository.getTransactionById(1L);
        assertThat(fetchedTransaction).isNotNull();
        assertThat(fetchedTransaction.getId()).isEqualTo(1L);
        assertThat(fetchedTransaction.getType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(fetchedTransaction.getAmount()).isEqualTo(1000.00);
    }

    @Test
    void getTransactionById_notFound() {
        assertNull(transactionRepository.getTransactionById(999L));
    }

    @Test
    void getTransactionsByUserId() {
        Account account1 = new Account(1L, AccountType.DEBIT, 1500.00);
        accountRepository.save(account1);

        Account account2 = new Account(2L, AccountType.CREDIT, 2000.00);
        accountRepository.save(account2);

        Transaction transaction1 = Transaction.builder()
                .type(TransactionType.TRANSFER)
                .amount(1500.00)
                .timestamp(LocalDateTime.now())
                .sourceAccountId(account1.getAccountUUID())
                .targetAccountId(account2.getAccountUUID())
                .build();

        Transaction transaction2 = Transaction.builder()
                .type(TransactionType.WITHDRAW)
                .amount(200.00)
                .timestamp(LocalDateTime.now())
                .sourceAccountId(account1.getAccountUUID())
                .targetAccountId(account2.getAccountUUID())
                .build();

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        List<Transaction> transactions = transactionRepository.getTransactionsByUserId(1L);

        assertThat(transactions.size()).isEqualTo(2);
        assertThat(transactions).extracting(Transaction::getType).contains(TransactionType.TRANSFER, TransactionType.WITHDRAW);
    }
}
