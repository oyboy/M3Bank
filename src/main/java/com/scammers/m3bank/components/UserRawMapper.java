package com.scammers.m3bank.components;

import com.scammers.m3bank.enums.Role;
import com.scammers.m3bank.enums.TransactionType;
import com.scammers.m3bank.models.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRawMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        Role role = Role.valueOf(rs.getString("role").toUpperCase());
        return new User(
                rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("password"),
                role
        );
    }
}
