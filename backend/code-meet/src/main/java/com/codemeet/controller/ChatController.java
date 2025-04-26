package com.codemeet.controller;

import com.codemeet.service.ChatService;
import com.codemeet.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    private final ChatService chatService;
    private final MessageService messageService;
    
    public ChatController(
        ChatService chatService,
        MessageService messageService
    ) {
        this.chatService = chatService;
        this.messageService = messageService;
    }
    
    @GetMapping("/{chatId}")
    public ResponseEntity<?> getChatById(
        @PathVariable Integer chatId,
        @RequestParam String chatType
    ) {
        return ResponseEntity.ok(
            chatType.equals("peer") ?
                chatService.getPeerChatById(chatId) :
                chatService.getRoomChatById(chatId)
        );
    }
    
    @GetMapping("/{ownerId}/all")
    public ResponseEntity<?> getAllChatsByOwnerId(
        @PathVariable Integer ownerId,
        @RequestParam String chatType
    ) {
        return ResponseEntity.ok(
            chatType.equals("peer") ?
                chatService.getAllPeerChatsByOwnerId(ownerId) :
                chatService.getAllRoomChatsByOwnerId(ownerId)
        );
    }
    
    @GetMapping("/{ownerId}/{otherId}")
    public ResponseEntity<?> getChatByOwnerIdAndOtherId(
        @PathVariable Integer ownerId,
        @PathVariable Integer otherId, // (Peer or Room)
        @RequestParam String chatType
    ) {
        return ResponseEntity.ok(
            chatType.equals("peer") ?
                chatService.getPeerChatByOwnerIdAndPeerId(ownerId, otherId) :
                chatService.getRoomChatByOwnerIdAndRoomId(ownerId, otherId)
        );
    }
    
    @GetMapping("/{chatId}/messages")
    public ResponseEntity<?> getAllMessagesOfChatByChatId(
        @PathVariable Integer chatId
    ) {
        return ResponseEntity.ok(messageService.getAllMessagesOfChatByChatId(chatId));
    }
}
