package com.scammers.m3bank.repositories;

import com.scammers.m3bank.components.UserRawMapper;
import com.scammers.m3bank.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserRawMapper userRawMapper;

    public void save(User user){
        String command = "INSERT INTO users (first_name, last_name, email, password) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(command, user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
    }

    public User findByEmail(String email){
        String command = "SELECT * FROM users WHERE email = ?";
        List<User> users = jdbcTemplate.query(command, userRawMapper, email);
        return DataAccessUtils.singleResult(users);
    }

    public User findById(Long id){
        String command = "SELECT * FROM users WHERE id = ?";
        List<User> users = jdbcTemplate.query(command, userRawMapper, id);
        return DataAccessUtils.singleResult(users);
    }
}
