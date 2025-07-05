CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    account_uuid VARCHAR(255),
    balance DECIMAL(15, 2),
    user_id BIGINT,
    account_type VARCHAR(25),
    blocked BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES users(id)
);