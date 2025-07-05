CREATE TABLE transaction_logs
(
    id               BIGSERIAL PRIMARY KEY,
    transaction_type VARCHAR(50),
    amount           FLOAT,
    timestamp        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    sourceAccountId  VARCHAR(40),
    targetAccountId  VARCHAR(40)
);