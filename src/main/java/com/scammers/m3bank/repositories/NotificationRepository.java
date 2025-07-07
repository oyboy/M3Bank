package com.scammers.m3bank.repositories;

import com.scammers.m3bank.components.NotificationRawMapper;
import com.scammers.m3bank.models.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NotificationRawMapper rawMapper;

    public void save(Notification notification) {
        /*Этот метод нужно доработать. Полагаю, что в какой-то момент придётся обновить сообщение как прочтённое, чтобы оно больше не попадалось в findByReceiverId*/
        jdbcTemplate.update(
                "INSERT INTO notifications (sender_id, receiver_id, message, sent_at, received) VALUES (?, ?, ?, ?, ?)",
                notification.getSenderId(),
                notification.getReceiverId(),
                notification.getMessage(),
                Timestamp.valueOf(notification.getSentAt()),
                notification.getReceived()
        );
    }

    public void markAsReceived(List<Long> ids) {
        jdbcTemplate.update("UPDATE notifications SET received = TRUE WHERE id = ANY (?)",
                new Object[]{ids.toArray(new Long[0])});
    }

    public List<Notification> findByReceiverId(Long userId) {
        String command = "SELECT * FROM notifications " +
                "WHERE receiver_id = ? AND received IS FALSE";
        return jdbcTemplate.query(command, rawMapper, userId);
    }
}
