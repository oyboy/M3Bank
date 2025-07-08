package com.scammers.m3bank.integration;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.SQLException;

@Testcontainers
public abstract class AbstractTestContainer {
    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("unittestdb")
                    .withUsername("test")
                    .withPassword("test");

    @BeforeAll
    public static void setup() {
        Flyway flyway = Flyway.configure().dataSource(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        ).load();
        flyway.migrate();
    }
    @BeforeEach
    public void before() {
        getJdbcTemplate().execute("DELETE FROM accounts");
        getJdbcTemplate().execute("DELETE FROM notifications");
        getJdbcTemplate().execute("DELETE FROM users");
        getJdbcTemplate().execute("DELETE FROM transaction_logs");
    }

    @DynamicPropertySource
    private static void setProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.datasource.url",
                postgreSQLContainer::getJdbcUrl
        );
        registry.add(
                "spring.datasource.username",
                postgreSQLContainer::getUsername
        );
        registry.add(
                "spring.datasource.password",
                postgreSQLContainer::getPassword
        );
    }
    protected static DataSource getDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(postgreSQLContainer.getDriverClassName())
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword())
                .build();
    }
    protected static JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }
}