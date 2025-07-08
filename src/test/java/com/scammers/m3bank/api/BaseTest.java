package com.scammers.m3bank.api;

import com.scammers.m3bank.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@AutoConfigureMockMvc
public abstract class BaseTest {
    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    void setupSecurityContext() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("user@example.com");
        testUser.setPassword("password");

        var auth = new UsernamePasswordAuthenticationToken(testUser, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }
}
