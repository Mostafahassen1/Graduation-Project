package com.codemeet.controller;

import com.codemeet.entity.NotificationType;
import com.codemeet.service.MessageService;
import com.codemeet.service.NotificationService;
import com.codemeet.utils.dto.chat.PeerMessageRequest;
import com.codemeet.utils.dto.chat.RoomMessageRequest;
import com.codemeet.utils.dto.notification.NotificationInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@RequiredArgsConstructor
@Controller
public class WebSocketController {
    
    private final MessageService messageService;
    private final NotificationService notificationService;
    
    @MessageMapping("/peer-chat")
    public void sendToPeer(@Payload PeerMessageRequest messageRequest) {
        messageService.saveAndSendToPeer(messageRequest);
    }
    
    @MessageMapping("/room-chat")
    public void sendToRoom(@Payload RoomMessageRequest messageRequest) {
        messageService.saveAndSendToRoom(messageRequest);
    }
    
    @PostMapping("/test.notification/{userId}")
    public void testNotification(@PathVariable Integer userId) {
        System.out.println("Sending notification to: " + userId);
        notificationService.sendToUser(new NotificationInfoResponse(
            Map.ofEntries(),
            userId,
            NotificationType.TEST
        ));
    }
    
    @PostMapping("/test.message/{userId}")
    public void testMessage(@PathVariable Integer userId) {
        System.out.println("Sending message to: " + userId);
    }
}
