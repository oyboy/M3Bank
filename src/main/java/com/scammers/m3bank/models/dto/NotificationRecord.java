package com.scammers.m3bank.models.dto;

public record NotificationRecord(
        Long id,
        String message,
        String sender
) {
}
