package com.scammers.m3bank.integration;

import com.scammers.m3bank.components.NotificationRawMapper;
import com.scammers.m3bank.models.Notification;
import com.scammers.m3bank.repositories.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationRepositoryTest extends AbstractTestContainer {
    private NotificationRepository notificationRepository;

    @BeforeEach
    void setUp() {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        NotificationRawMapper rawMapper = new NotificationRawMapper();
        notificationRepository = new NotificationRepository(jdbcTemplate, rawMapper);
        jdbcTemplate.update("INSERT INTO users (id, first_name, last_name, email, password) VALUES (?, ?, ?, ?, ?)", 1L, "John", "Doe", "john.doe@example.com", "pass");
        jdbcTemplate.update("INSERT INTO users (id, first_name, last_name, email, password) VALUES (?, ?, ?, ?, ?)", 2L, "John2", "Doe", "john2.doe@example.com", "pass2");
    }

    @Test
    void save_shouldPersistNotification() {
        Notification notification = Notification.builder()
                .senderId(1L)
                .receiverId(2L)
                .message("Test message")
                .sentAt(LocalDateTime.now())
                .received(false)
                .build();

        Notification saved = notificationRepository.save(notification);

        assertThat(saved.getId()).isNotNull();

        List<Notification> found = notificationRepository.findByReceiverId(2L);
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getMessage()).isEqualTo("Test message");
    }

    @Test
    void findByReceiverId_shouldReturnUnreceivedNotifications() {
        Notification notification = Notification.builder()
                .senderId(1L)
                .receiverId(2L)
                .message("Hello")
                .sentAt(LocalDateTime.now())
                .received(false)
                .build();

        notificationRepository.save(notification);

        List<Notification> result = notificationRepository.findByReceiverId(2L);
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getReceiverId()).isEqualTo(2L);
        assertThat(result.get(0).getReceived()).isFalse();
    }

    @Test
    void markAsReceived_shouldUpdateReceivedFlag() {
        Notification notification = Notification.builder()
                .senderId(1L)
                .receiverId(2L)
                .message("Check me")
                .sentAt(LocalDateTime.now())
                .received(false)
                .build();

        Notification saved = notificationRepository.save(notification);

        notificationRepository.markAsReceived(List.of(saved.getId()));

        List<Notification> after = notificationRepository.findByReceiverId(2L);
        assertThat(after).isEmpty();
    }
}

