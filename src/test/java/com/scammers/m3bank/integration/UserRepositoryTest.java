package com.scammers.m3bank.integration;

import com.scammers.m3bank.components.UserRawMapper;
import com.scammers.m3bank.enums.Role;
import com.scammers.m3bank.models.User;
import com.scammers.m3bank.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest extends AbstractTestContainer {
    private JdbcTemplate jdbcTemplate;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate = getJdbcTemplate();
        userRepository = new UserRepository(jdbcTemplate, new UserRawMapper());
    }

    @Test
    void saveUser() {
        User user = new User(1L, "John", "Doe", "john.doe@example.com", "password123", Role.ROLE_USER);
        userRepository.save(user);
        List<User> usersAfter = jdbcTemplate.query("SELECT * FROM users WHERE email = ?", new UserRawMapper(), "john.doe@example.com");
        assertThat(usersAfter.size()).isEqualTo(1);
        assertThat(usersAfter.get(0).getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    void findByEmail() {
        User user = new User(1L, "John", "Doe", "john.doe@example.com", "password123", Role.ROLE_USER);
        userRepository.save(user);
        User foundUser = userRepository.findByEmail("john.doe@example.com");
        assertNotNull(foundUser);
        assertThat(foundUser.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    void willReturnEmptyWhenEmailDoesNotExist() {
        User user = userRepository.findByEmail("nonexistent@mail.com");
        assertNull(user);
    }

    @Test
    void existsUserWithEmail() {
        User user = new User(1L, "John", "Doe", "john.doe@example.com", "password123", Role.ROLE_USER);
        userRepository.save(user);
        boolean exists = userRepository.findByEmail("john.doe@example.com") != null;
        assertTrue(exists);
    }

    @Test
    void existsUserWithId() {
        User user = new User(1L, "John", "Doe", "john.doe@example.com", "password123", Role.ROLE_USER);
        userRepository.save(user);
        Long id = jdbcTemplate.queryForObject("SELECT id FROM users WHERE email = ?", Long.class, "john.doe@example.com");
        boolean exists = userRepository.findById(id) != null;
        assertTrue(exists);
    }

    @Test
    void updateUser() {
        User user = new User(1L, "John", "Doe", "john.doe@example.com", "password123", Role.ROLE_USER);
        userRepository.save(user);
        Long id = jdbcTemplate.queryForObject("SELECT id FROM users WHERE email = ?", Long.class, "john.doe@example.com");
        User updatedUser = new User(id, "John", "Doe", "john.updated@example.com", "newpassword123", Role.ROLE_USER);
        userRepository.save(updatedUser);
        User foundUser = userRepository.findById(id);
        assertNotNull(foundUser);
        assertThat(foundUser.getEmail()).isEqualTo("john.updated@example.com");
    }
}