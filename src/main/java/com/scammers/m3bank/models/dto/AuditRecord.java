package com.scammers.m3bank.models.dto;

import java.time.LocalDateTime;

public record AuditRecord(
        String username,
        String action,
        LocalDateTime timestamp
) {
}
