package com.scammers.m3bank.integration;

import org.junit.jupiter.api.Test;

public class TestcontainersTest extends AbstractTestContainer {
    @Test
    void canStartPostgresDb() {
        assert POSTGRES_CONTAINER.isRunning();
        assert POSTGRES_CONTAINER.isCreated();
    }
}
