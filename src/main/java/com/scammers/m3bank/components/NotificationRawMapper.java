package com.scammers.m3bank.components;

import com.scammers.m3bank.models.Notification;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class NotificationRawMapper implements RowMapper<Notification> {
    @Override
    public Notification mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Notification(
                rs.getLong("id"),
                rs.getString("message"),
                rs.getLong("sender_id"),
                rs.getLong("receiver_id"),
                rs.getTimestamp("sent_at").toLocalDateTime(),
                rs.getBoolean("received")
        );
    }
}
