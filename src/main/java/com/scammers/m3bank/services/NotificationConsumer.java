package com.scammers.m3bank.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scammers.m3bank.models.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topic.notifications}", groupId = "notification-group")
    public void consume(String rawMessage) {
        Notification notification = parseNotification(rawMessage);
        notificationService.save(notification);
    }

    private Notification parseNotification(String json) {
        try {
            return objectMapper.readValue(json, Notification.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Не удалось разобрать уведомление", e);
        }
    }
}
