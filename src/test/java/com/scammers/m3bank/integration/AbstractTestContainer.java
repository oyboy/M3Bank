package com.scammers.m3bank.integration;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractTestContainer {
    private static JdbcTemplate jdbcTemplate;
    protected static final SingletonPostgresContainer POSTGRES_CONTAINER = SingletonPostgresContainer.getInstance();

    static {
        POSTGRES_CONTAINER.start();
    }

    @BeforeAll
    public static void setup() {
        Flyway flyway = Flyway.configure().dataSource(
                POSTGRES_CONTAINER.getJdbcUrl(),
                POSTGRES_CONTAINER.getUsername(),
                POSTGRES_CONTAINER.getPassword()
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
                POSTGRES_CONTAINER::getJdbcUrl
        );
        registry.add(
                "spring.datasource.username",
                POSTGRES_CONTAINER::getUsername
        );
        registry.add(
                "spring.datasource.password",
                POSTGRES_CONTAINER::getPassword
        );
    }
    protected static DataSource getDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(POSTGRES_CONTAINER.getDriverClassName())
                .url(POSTGRES_CONTAINER.getJdbcUrl())
                .username(POSTGRES_CONTAINER.getUsername())
                .password(POSTGRES_CONTAINER.getPassword())
                .build();
    }
    protected static JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            jdbcTemplate = new JdbcTemplate(getDataSource());
        }
        return jdbcTemplate;
    }
}