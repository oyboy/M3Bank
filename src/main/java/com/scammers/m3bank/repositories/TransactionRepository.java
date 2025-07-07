package com.scammers.m3bank.repositories;

import com.scammers.m3bank.components.TransactionRawMapper;
import com.scammers.m3bank.models.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final TransactionRawMapper transactionRawMapper;

    public void save(Transaction transaction) {
        String command = "INSERT INTO transaction_logs (transaction_type, amount, timestamp, source_account_id, target_account_id)"
                + " VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(command,
                transaction.getType().toString(),
                transaction.getAmount(),
                transaction.getTimestamp(),
                transaction.getSourceAccountId(),
                transaction.getTargetAccountId()
        );
    }

    public Transaction getTransactionById(Long transactionId) {
        String command = "SELECT * FROM transaction_logs WHERE id = ?";
        List<Transaction> transactions = jdbcTemplate.query(command, transactionRawMapper, transactionId);
        return DataAccessUtils.singleResult(transactions);
    }

    public List<Transaction> getTransactionsByUserId(long userId) {
        String sql = "SELECT t.id, t.transaction_type, t.amount, t.timestamp, t.source_account_id, t.target_account_id " +
                "FROM transaction_logs t " +
                "JOIN accounts a ON t.source_account_id = a.account_uuid " +
                "WHERE a.user_id = ?";

        return jdbcTemplate.query(sql, new Object[]{userId}, transactionRawMapper);
    }
}
