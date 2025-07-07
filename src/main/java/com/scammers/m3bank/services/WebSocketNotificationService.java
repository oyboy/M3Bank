package com.scammers.m3bank.services;

import com.scammers.m3bank.models.dto.NotificationRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketNotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendToUser(Long userId, NotificationRecord record) {
        String destination = "/topic/notifications/" + userId;
        messagingTemplate.convertAndSend(destination, record);
    }
}