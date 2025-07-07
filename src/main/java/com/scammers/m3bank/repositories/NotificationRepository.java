package com.scammers.m3bank.repositories;

import com.scammers.m3bank.components.NotificationRawMapper;
import com.scammers.m3bank.models.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NotificationRawMapper rawMapper;

    public Notification save(Notification notification) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO notifications (sender_id, receiver_id, message, sent_at, received) VALUES (?, ?, ?, ?, ?)",
                    new String[]{"id"}
            );
            ps.setLong(1, notification.getSenderId());
            ps.setLong(2, notification.getReceiverId());
            ps.setString(3, notification.getMessage());
            ps.setTimestamp(4, Timestamp.valueOf(notification.getSentAt()));
            ps.setBoolean(5, notification.getReceived());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            notification.setId(key.longValue());
        } else {
            throw new RuntimeException("Не удалось получить ID после сохранения уведомления");
        }
        return notification;
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
