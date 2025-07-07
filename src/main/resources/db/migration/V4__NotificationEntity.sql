CREATE TABLE notifications
(
    id      BIGSERIAL PRIMARY KEY,
    message VARCHAR(100),
    sender_id BIGINT,
    receiver_id BIGINT,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    received BOOLEAN,
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (receiver_id) REFERENCES users(id)
);