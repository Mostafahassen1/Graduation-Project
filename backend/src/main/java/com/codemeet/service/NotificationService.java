package com.codemeet.service;

import com.codemeet.entity.Notification;
import com.codemeet.repository.NotificationRepository;
import com.codemeet.utils.dto.notification.NotificationInfo;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
@AllArgsConstructor
@Service
public class NotificationService {
    
    public static final String DEFAULT_DESTINATION = "/notifications";

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;
    

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotifications(Integer userId) {
        List<Notification> notifications = notificationRepository.getNotifications(userId);
        return notifications;
    }
    
    public void sendToUser(String destination, NotificationInfo notification) {
        messagingTemplate.convertAndSendToUser( // Maps to /user/{userId}/notification
            notification.receiverId().toString(), destination, notification);
    }
    
    public void sendToUser(NotificationInfo notification) {
        sendToUser(DEFAULT_DESTINATION, notification);
    }
    
    public void sendToUsers(Iterable<NotificationInfo> notifications) {
        for (NotificationInfo notification : notifications) {
            sendToUser(notification);
        }
    }
}
