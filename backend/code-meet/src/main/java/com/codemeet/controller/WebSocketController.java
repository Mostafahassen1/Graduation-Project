package com.codemeet.controller;

import com.codemeet.service.MessageService;
import com.codemeet.service.RoomChatService;
import com.codemeet.utils.dto.chat.MessageInfo;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    
    public WebSocketController(
        SimpMessagingTemplate messagingTemplate,
        MessageService messageService
    ) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
    }
    
    @MessageMapping("/message")  // Clients send messages to "/app/message" at backend
    @SendTo("/topic/response")  // Broadcast to subscribers on "/topic/response" at frontend
    public String handleMessage(String message) {
        System.out.println(message);
        return  message;
    }
    
    @MessageMapping("/room-chat")
    public void sendToRoom(@Payload MessageInfo messageInfo) {
        messageInfo = messageService.save(messageInfo);
        
        String destination = "/chat/" + messageInfo.chatId();
        messagingTemplate.convertAndSend(destination, messageInfo);
    }
}
