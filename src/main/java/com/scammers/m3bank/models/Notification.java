package com.scammers.m3bank.models;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
public class Notification {
    private Long id;
    private String message;
    private Long senderId;
    private Long receiverId;
    private LocalDateTime sentAt;
    private Boolean received;
}
