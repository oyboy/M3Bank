package com.scammers.m3bank.integration;

import org.junit.jupiter.api.Test;

public class TestcontainersTest extends AbstractTestContainer {
    @Test
    void canStartPostgresDb() {
        assert postgreSQLContainer.isRunning();
        assert postgreSQLContainer.isCreated();
    }
}
