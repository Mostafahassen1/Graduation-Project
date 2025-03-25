package com.codemeet.service;

import com.codemeet.entity.Notification;
import com.codemeet.repository.NotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    
    public static final String DEFAULT_DESTINATION = "/notifications";

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;
    
    public NotificationService(
        SimpMessagingTemplate messagingTemplate,
        NotificationRepository notificationRepository
    ) {
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
    }
    
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotifications(Integer userId) {
        List<Notification> notifications = notificationRepository.getNotifications(userId);
        return notifications;
    }
    
    public void sendToUser(String destination, Notification notification) {
        messagingTemplate.convertAndSendToUser( // Maps to /user/{userId}/notification
            notification.getReceiver().getId().toString(), destination, notification);
    }
    
    public void sendToUser(Notification notification) {
        sendToUser(DEFAULT_DESTINATION, notification);
    }
    
    public void sendToUsers(Iterable<Notification> notifications) {
        for (Notification notification : notifications) {
            sendToUser(notification);
        }
    }
}
