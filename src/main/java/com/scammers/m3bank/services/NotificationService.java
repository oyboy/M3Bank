package com.scammers.m3bank.services;

import com.scammers.m3bank.annotations.Auditable;
import com.scammers.m3bank.models.Notification;
import com.scammers.m3bank.models.User;
import com.scammers.m3bank.models.dto.NotificationRecord;
import com.scammers.m3bank.repositories.NotificationRepository;
import com.scammers.m3bank.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;
    private final UserRepository userRepository;

    @Auditable(action = "Запись уведомления в базу")
    public Notification save(Notification notification) {
        return repository.save(notification);
    }

    @Auditable(action = "Получение непрочитанных уведомлений")
    public List<Notification> getNotificationsByReceiverId(Long id){
        List<Notification> notifications = repository.findByReceiverId(id);
        log.info("Found " + notifications.size() + " notifications");
        if (!notifications.isEmpty()) {
            repository.markAsReceived(notifications.stream()
                    .map(Notification::getId)
                    .toList());
        }
        log.info("Marked as received");
        return notifications;
    }

    public NotificationRecord convertToRecord(Notification notification) {
        User user = userRepository.findById(notification.getSenderId());
        String lastInitial = user.getLastName() != null && !user.getLastName().isEmpty()
                ? user.getLastName().substring(0, 1) : "";
        return new NotificationRecord(notification.getId(), notification.getMessage(), user.getFirstName() + " " + lastInitial + ".");
    }

    @Auditable(action = "Пометка уведомления о прочтении")
    public void markAsRead(Long id) {
        repository.markAsReceived(List.of(id));
    }
}
