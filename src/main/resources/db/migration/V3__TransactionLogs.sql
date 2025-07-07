CREATE TABLE transaction_logs
(
    id               BIGSERIAL PRIMARY KEY,
    transaction_type VARCHAR(50),
    amount           DOUBLE PRECISION,
    timestamp        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    source_account_id  VARCHAR(40),
    target_account_id  VARCHAR(40)
);