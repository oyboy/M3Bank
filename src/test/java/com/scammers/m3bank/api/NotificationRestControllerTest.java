package com.scammers.m3bank.api;

import com.scammers.m3bank.controllers.NotificationRestController;
import com.scammers.m3bank.models.Notification;
import com.scammers.m3bank.models.dto.NotificationRecord;
import com.scammers.m3bank.services.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationRestController.class)
class NotificationRestControllerTest extends BaseTest {
    @MockBean
    private NotificationService notificationService;

    @Test
    void testGetNotifications_returnsNotificationRecords() throws Exception {
        Long userId = 1L;

        Notification notification1 = new Notification();
        notification1.setId(10L);
        notification1.setMessage("Hello");
        notification1.setSenderId(2L);

        Notification notification2 = new Notification();
        notification2.setId(11L);
        notification2.setMessage("World");
        notification2.setSenderId(3L);

        NotificationRecord record1 = new NotificationRecord(10L, "Hello", "Alice A.");
        NotificationRecord record2 = new NotificationRecord(11L, "World", "Bob B.");

        List<Notification> notifications = List.of(notification1, notification2);

        when(notificationService.getNotificationsByReceiverId(userId)).thenReturn(notifications);
        when(notificationService.convertToRecord(notification1)).thenReturn(record1);
        when(notificationService.convertToRecord(notification2)).thenReturn(record2);

        mockMvc.perform(get("/api/v1/notifications")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].message").value("Hello"))
                .andExpect(jsonPath("$[0].sender").value("Alice A."))
                .andExpect(jsonPath("$[1].id").value(11))
                .andExpect(jsonPath("$[1].message").value("World"))
                .andExpect(jsonPath("$[1].sender").value("Bob B."));

        verify(notificationService).getNotificationsByReceiverId(userId);
        verify(notificationService).convertToRecord(notification1);
        verify(notificationService).convertToRecord(notification2);
    }

    @Test
    void testMarkAsRead_shouldReturnOk() throws Exception {
        Long notificationId = 42L;

        mockMvc.perform(post("/api/v1/notifications/read/{id}", notificationId)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(notificationService).markAsRead(notificationId);
    }
}

