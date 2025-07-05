package com.scammers.m3bank.components;

import com.scammers.m3bank.enums.AccountType;
import com.scammers.m3bank.models.Account;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AccountRawMapper implements RowMapper<Account> {
    @Override
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        AccountType accountType = AccountType.valueOf(rs.getString("account_type").toUpperCase());

        return new Account(
                rs.getLong("id"),
                rs.getString("account_uuid"),
                rs.getDouble("balance"),
                rs.getLong("user_id"),
                accountType,
                rs.getBoolean("blocked")
        );
    }
}
