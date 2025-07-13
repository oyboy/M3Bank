package com.scammers.m3bank.integration;

import org.testcontainers.containers.PostgreSQLContainer;

public class SingletonPostgresContainer extends PostgreSQLContainer<SingletonPostgresContainer> {
    private static final SingletonPostgresContainer INSTANCE = new SingletonPostgresContainer();

    private SingletonPostgresContainer() {
        super("postgres:latest");
        withDatabaseName("unittestdb");
        withUsername("test");
        withPassword("test");
    }

    public static SingletonPostgresContainer getInstance() {
        return INSTANCE;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", getJdbcUrl());
        System.setProperty("DB_USERNAME", getUsername());
        System.setProperty("DB_PASSWORD", getPassword());
    }

    @Override
    public void stop() {
    }
}

