package com.scammers.m3bank.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scammers.m3bank.models.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.topic.notifications}")
    private String topic;

    public void send(Notification notification) {
        try {
            String payload = objectMapper.writeValueAsString(notification);
            kafkaTemplate.send(topic, notification.getReceiverId().toString(), payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка при сериализации уведомления", e);
        }
    }
}
