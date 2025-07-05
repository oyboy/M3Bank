package com.scammers.m3bank.components;

import com.scammers.m3bank.enums.TransactionType;
import com.scammers.m3bank.models.Transaction;
import org.springframework.stereotype.Component;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TransactionRawMapper implements RowMapper<Transaction> {
    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        TransactionType type = TransactionType.valueOf(rs.getString("type").toUpperCase());
        return new Transaction(
                rs.getLong("id"),
                type,
                rs.getDouble("amount"),
                rs.getTimestamp("timestamp").toLocalDateTime(),
                rs.getString("source_account_id"),
                rs.getString("target_account_id")
        );
    }
}
