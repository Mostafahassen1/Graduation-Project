package com.codemeet.controller;

import com.codemeet.service.RoomChatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    private final RoomChatService roomChatService;
    
    public ChatController(RoomChatService roomChatService) {
        this.roomChatService = roomChatService;
    }
}
