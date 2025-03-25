package com.codemeet.controller;

import com.codemeet.service.MessageService;
import com.codemeet.service.RoomChatService;
import com.codemeet.utils.dto.chat.MessageInfo;
import com.codemeet.utils.dto.chat.RoomChatInfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/room-chat")
public class RoomChatController {
    
    private final RoomChatService roomChatService;
    private final MessageService messageService;
    
    public RoomChatController(
        RoomChatService roomChatService,
        MessageService messageService
    ) {
        this.roomChatService = roomChatService;
        this.messageService = messageService;
    }
    
    @GetMapping("/{chatId}")
    public ResponseEntity<RoomChatInfoResponse> getRoomChatById(
        @PathVariable Integer chatId
    ) {
        return ResponseEntity.ok(roomChatService.getRoomChatById(chatId));
    }
    
    @GetMapping("/room/{roomId}")
    public ResponseEntity<RoomChatInfoResponse> getRoomChatByRoomId(
        @PathVariable Integer roomId
    ) {
        return ResponseEntity.ok(roomChatService.getRoomChatByRoomId(roomId));
    }
    
    @GetMapping("/user/{userId}/all")
    public ResponseEntity<List<RoomChatInfoResponse>> getAllRoomChatsOfUser(
        @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(roomChatService.getAllRoomChatsOfUser(userId));
    }
    
    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<MessageInfo>> getAllMessagesOfChat(
        @PathVariable Integer chatId
    ) {
        return ResponseEntity.ok(messageService.getAllMessagesOfChat(chatId));
    }
}
