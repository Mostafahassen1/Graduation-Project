package com.codemeet.controller;

import com.codemeet.service.MessageService;
import com.codemeet.service.NotificationService;
import com.codemeet.utils.dto.chat.PeerMessageRequest;
import com.codemeet.utils.dto.chat.RoomMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

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
}
