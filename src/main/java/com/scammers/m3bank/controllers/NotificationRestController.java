package com.scammers.m3bank.controllers;

import com.scammers.m3bank.models.dto.NotificationRecord;
import com.scammers.m3bank.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationRestController {
    private final NotificationService notificationService;
    @GetMapping
    public List<NotificationRecord> getNotifications(@RequestParam Long userId) {
        return notificationService.getNotificationsByReceiverId(userId)
                .stream()
                .map(notificationService::convertToRecord)
                .collect(Collectors.toList());
    }
}
