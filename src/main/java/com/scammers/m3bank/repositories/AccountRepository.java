package com.scammers.m3bank.repositories;

import com.scammers.m3bank.components.AccountRawMapper;
import com.scammers.m3bank.models.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AccountRepository {
    private final JdbcTemplate jdbcTemplate;
    private final AccountRawMapper accountRawMapper;

    public void save(Account account) {
        if (findByUuid(account.getAccountUUID()) == null) {
            String command = "INSERT INTO accounts (account_uuid, balance, user_id, account_type, blocked) " +
                    "VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(
                    command,
                    account.getAccountUUID(),
                    account.getBalance(),
                    account.getUserId(),
                    account.getAccountType().toString(),
                    account.isBlocked()
            );
        }
        else{
            String command = "UPDATE accounts SET balance = ?, user_id = ?, account_type = ?, blocked = ? " +
                 "WHERE account_uuid = ?";
            jdbcTemplate.update(
                    command,
                    account.getBalance(),
                    account.getUserId(),
                    account.getAccountType().toString(),
                    account.isBlocked(),
                    account.getAccountUUID()
            );
        }
    }


    public List<Account> findAllByUserId(Long userId) {
        String command = "select * from accounts where user_id = ?";
        return jdbcTemplate.query(command, accountRawMapper, userId);
    }

    public Account findByUuid(String uuid) {
        String command = "select * from accounts where account_uuid = ?";
        List<Account> accounts = jdbcTemplate.query(command, accountRawMapper, uuid);
        return DataAccessUtils.singleResult(accounts);
    }

    public Long getUserId(String uuid) {
        String command = "SELECT user_id FROM accounts WHERE account_uuid = ?";
        return jdbcTemplate.queryForObject(command, Long.class, uuid);
    }

    public void delete(String uuid) {
        String command = "DELETE FROM accounts WHERE account_uuid = ?";
        jdbcTemplate.update(command, uuid);
    }

    public void delete(Long id) {
        String command = "DELETE FROM accounts WHERE id = ?";
        jdbcTemplate.update(command, id);
    }

    public void updateBalance(String uuid, Double newBalance) {
        String command = "UPDATE accounts SET balance = ? WHERE account_uuid = ?";
        jdbcTemplate.update(command, newBalance, uuid);
    }

    public void updateBalance(Long id, Double newBalance) {
        String command = "UPDATE accounts SET balance = ? WHERE id = ?";
        jdbcTemplate.update(command, newBalance, id);
    }
}
