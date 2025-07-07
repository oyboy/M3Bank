CREATE TABLE audit_logs
(
    id        BIGSERIAL PRIMARY KEY,
    username  VARCHAR(100),
    action    VARCHAR(255),
    timestamp TIMESTAMP
);