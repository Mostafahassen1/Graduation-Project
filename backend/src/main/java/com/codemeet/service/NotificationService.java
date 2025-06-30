package com.codemeet.service;

import com.codemeet.entity.*;
import com.codemeet.repository.NotificationRepository;
import com.codemeet.utils.dto.notification.NotificationInfoResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static jakarta.transaction.Transactional.TxType.REQUIRES_NEW;

@RequiredArgsConstructor
@Service
public class NotificationService {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;
    
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }
    
    public void deleteById(Integer notificationId) {
        notificationRepository.deleteById(notificationId);
    }
    
    public List<Notification> getAllNotificationEntitiesByReceiverId(Integer receiverId) {
        return notificationRepository.findAllByReceiverId(receiverId);
    }

    public List<NotificationInfoResponse> getAllNotificationsByReceiverId(Integer receiverId) {
        return getAllNotificationEntitiesByReceiverId(receiverId).stream()
            .map(NotificationInfoResponse::of)
            .toList();
    }
    
    public void send(NotificationInfoResponse notification) {
        messagingTemplate.convertAndSend( // Maps to /user/{userId}/notification
            "/notifications/" + notification.receiverId(), notification);
    }
    
    public void sendAll(Iterable<NotificationInfoResponse> notifications) {
        for (NotificationInfoResponse notification : notifications) {
            send(notification);
        }
    }
    
    @Transactional(REQUIRES_NEW)
    public void sendFriendshipRequestNotification(
        User requestSender,
        User notificationReceiver
    ) {
        Notification notification = Notification.builder()
            .info(Map.ofEntries(
                Map.entry("senderUsername", requestSender.getUsername()),
                Map.entry("senderFullName", requestSender.getFullName())
            ))
            .receiver(notificationReceiver)
            .type(NotificationType.FRIENDSHIP_REQUEST)
            .build();
        
        notificationRepository.save(notification);
        
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    System.out.println("Sending notification: " + notification);
                    send(NotificationInfoResponse.of(notification));
                }
            }
        );
    }
    
    @Transactional(REQUIRES_NEW)
    public void sendFriendshipAcceptedNotification(
        User requestAcceptor,
        User notificationReceiver
    ) {
        Notification notification = Notification.builder()
            .info(Map.ofEntries(
                Map.entry("acceptorUsername", requestAcceptor.getUsername()),
                Map.entry("acceptorFullName", requestAcceptor.getFullName())
            ))
            .receiver(notificationReceiver)
            .type(NotificationType.FRIENDSHIP_ACCEPTED)
            .build();
        
        notificationRepository.save(notification);
        
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    System.out.println("Sending notification: " + notification);
                    send(NotificationInfoResponse.of(notification));
                }
            }
        );
    }
    
    @Transactional(REQUIRES_NEW)
    public void sendMembershipRequestNotification(
        User requestSender,
        Room requestedRoom,
        User notificationReceiver
    ) {
        Notification notification = Notification.builder()
            .info(Map.ofEntries(
                Map.entry("roomId", requestedRoom.getId().toString()),
                Map.entry("roomName", requestedRoom.getName()),
                Map.entry("senderUsername", requestSender.getUsername()),
                Map.entry("senderFullName", requestSender.getFullName())
            ))
            .receiver(notificationReceiver)
            .type(NotificationType.MEMBERSHIP_REQUEST)
            .build();
        
        notificationRepository.save(notification);
        
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    System.out.println("Sending notification: " + notification);
                    send(NotificationInfoResponse.of(notification));
                }
            }
        );
    }
    
    @Transactional(REQUIRES_NEW)
    public void sendMembershipAcceptedNotification(
        Room requestedRoom,
        User notificationReceiver
    ) {
        Notification notification = Notification.builder()
            .info(Map.ofEntries(
                Map.entry("roomId", requestedRoom.getId().toString()),
                Map.entry("roomName", requestedRoom.getName())
            ))
            .receiver(notificationReceiver)
            .type(NotificationType.MEMBERSHIP_ACCEPTED)
            .build();
        
        notificationRepository.save(notification);
        
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    System.out.println("Sending notification: " + notification);
                    send(NotificationInfoResponse.of(notification));
                }
            }
        );
    }
    
    @Transactional(REQUIRES_NEW)
    public void sendMeetingScheduledNotification(
        Meeting scheduledMeeting,
        Iterable<Participant> participants
    ) {
        List<Notification> notifications = new ArrayList<>();
        for (Participant participant : participants) {
            notifications.add(Notification.builder()
                .info(Map.ofEntries(
                    Map.entry("meetingId", scheduledMeeting.getId().toString()),
                    Map.entry("meetingTitle", scheduledMeeting.getTitle()),
                    Map.entry("meetingDescription", scheduledMeeting.getDescription()),
                    Map.entry("creatorUsername", scheduledMeeting.getCreator().getUsername()),
                    Map.entry("creatorFullName", scheduledMeeting.getCreator().getFullName()),
                    Map.entry("startsAt", scheduledMeeting.getStartsAt().toString())
                ))
                .receiver(participant.getUser())
                .type(NotificationType.MEETING_SCHEDULED)
                .build());
        }
        
        notificationRepository.saveAll(notifications);
        
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    sendAll(notifications.stream()
                        .map(NotificationInfoResponse::of)
                        .toList()
                    );
                }
            }
        );
    }
    
    @Transactional(REQUIRES_NEW)
    public void sendMeetingStartedNotification(
        Meeting scheduledMeeting,
        Iterable<Participant> participants
    ) {
        List<Notification> notifications = new ArrayList<>();
        for (Participant participant : participants) {
            notifications.add(Notification.builder()
                .info(Map.ofEntries(
                    Map.entry("meetingId", scheduledMeeting.getId().toString()),
                    Map.entry("meetingTitle", scheduledMeeting.getTitle()),
                    Map.entry("meetingDescription", scheduledMeeting.getDescription()),
                    Map.entry("creatorUsername", scheduledMeeting.getCreator().getUsername()),
                    Map.entry("creatorFullName", scheduledMeeting.getCreator().getFullName()),
                    Map.entry("startsAt", scheduledMeeting.getStartsAt().toString())
                ))
                .receiver(participant.getUser())
                .type(NotificationType.MEETING_STARTED)
                .build());
        }
        
        notificationRepository.saveAll(notifications);
        
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    sendAll(notifications.stream()
                        .map(NotificationInfoResponse::of)
                        .toList()
                    );
                }
            }
        );
    }
    
    @Transactional(REQUIRES_NEW)
    public void sendPeerMessageNotification(Message message) {
        Notification notification = Notification.builder()
            .info(Map.ofEntries(
                Map.entry("chatId", message.getChat().getId().toString()),
                Map.entry("senderFirstName", message.getSender().getFirstName()),
                Map.entry("senderLastName", message.getSender().getLastName()),
                Map.entry("senderUsername", message.getSender().getUsername()),
                Map.entry("content", message.getContent())
            ))
            .receiver(message.getChat().getOwner())
            .type(NotificationType.PEER_MESSAGE)
            .build();
        
        notificationRepository.save(notification);
        
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    System.out.println("Sending notification: " + notification);
                    send(NotificationInfoResponse.of(notification));
                }
            }
        );
    }
    
    @Transactional(REQUIRES_NEW)
    public void sendRoomMessageNotification(
        Iterable<Message> messages
    ) {
        List<Notification> notifications = new ArrayList<>();
        for (Message message : messages) {
            notifications.add(Notification.builder()
                .info(Map.ofEntries(
                    Map.entry("chatId", message.getChat().getId().toString()),
                    Map.entry("roomName", ((RoomChat) message.getChat()).getRoom().getName()),
                    Map.entry("senderFirstName", message.getSender().getFirstName()),
                    Map.entry("senderLastName", message.getSender().getLastName()),
                    Map.entry("senderUsername", message.getSender().getUsername()),
                    Map.entry("content", message.getContent())
                ))
                .receiver(message.getChat().getOwner())
                .type(NotificationType.ROOM_MESSAGE)
                .build());
        }
        
        notificationRepository.saveAll(notifications);
        
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    sendAll(notifications.stream()
                        .map(NotificationInfoResponse::of)
                        .toList()
                    );
                }
            }
        );
    }
}
