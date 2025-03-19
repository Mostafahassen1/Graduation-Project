package com.codemeet.controller;

import com.codemeet.service.ChatService;
import com.codemeet.utils.dto.chat.MessageInfoResponse;
import com.codemeet.utils.dto.chat.RoomChatInfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    private final ChatService chatService;
    
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    
    @GetMapping("/room/{userId}")
    public ResponseEntity<List<RoomChatInfoResponse>> getAllRoomChatsOfUser(
        @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(chatService.getAllRoomChatsByUserId(userId));
    }
    
    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<MessageInfoResponse>> getAllMessagesOfChat(
        @PathVariable Integer chatId
    ) {
        return ResponseEntity.ok(chatService.getAllMessagesByChatId(chatId));
    }
}
