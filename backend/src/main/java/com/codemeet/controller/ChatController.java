package com.codemeet.controller;

import com.codemeet.service.ChatService;
import com.codemeet.service.MessageService;
import com.codemeet.utils.dto.chat.PeerChatInfoResponse;
import com.codemeet.utils.dto.chat.PeerMessageResponse;
import com.codemeet.utils.dto.chat.RoomChatInfoResponse;
import com.codemeet.utils.dto.chat.RoomMessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    private final ChatService chatService;
    private final MessageService messageService;

    @GetMapping("/peer/{chatId}")
    public ResponseEntity<PeerChatInfoResponse> getPeerChatById(
        @PathVariable Integer chatId
    ) {
        return ResponseEntity.ok(chatService.getPeerChatById(chatId));
    }
    
    @GetMapping("/room/{chatId}")
    public ResponseEntity<RoomChatInfoResponse> getRoomChatById(
        @PathVariable Integer chatId
    ) {
        return ResponseEntity.ok(chatService.getRoomChatById(chatId));
    }
    
    @GetMapping("/peer/{ownerId}/all")
    public ResponseEntity<List<PeerChatInfoResponse>> getAllPeerChatsByOwnerId(
        @PathVariable Integer ownerId
    ) {
        return ResponseEntity.ok(chatService.getAllPeerChatsByOwnerId(ownerId));
    }
    
    @GetMapping("/room/{ownerId}/all")
    public ResponseEntity<List<RoomChatInfoResponse>> getAllRoomChatsByOwnerId(
        @PathVariable Integer ownerId
    ) {
        return ResponseEntity.ok(chatService.getAllRoomChatsByOwnerId(ownerId));
    }
    
    @GetMapping("/peer/{ownerId}/{peerId}")
    public ResponseEntity<PeerChatInfoResponse> getChatByOwnerIdAndPeerId(
        @PathVariable Integer ownerId,
        @PathVariable Integer peerId
    ) {
        return ResponseEntity.ok(chatService.getPeerChatByOwnerIdAndPeerId(ownerId, peerId));
    }
    
    @GetMapping("/room/{ownerId}/{roomId}")
    public ResponseEntity<PeerChatInfoResponse> getChatByOwnerIdAndRoomId(
        @PathVariable Integer ownerId,
        @PathVariable Integer roomId
    ) {
        return ResponseEntity.ok(chatService.getPeerChatByOwnerIdAndPeerId(ownerId, roomId));
    }
    
    @GetMapping("/peer/{chatId}/messages")
    public ResponseEntity<List<PeerMessageResponse>> getAllMessagesOfPeerChatByChatId(
        @PathVariable Integer chatId
    ) {
        return ResponseEntity.ok(messageService.getAllMessagesOfPeerChatByChatId(chatId));
    }
    
    @GetMapping("/room/{chatId}/messages")
    public ResponseEntity<List<RoomMessageResponse>> getAllMessagesOfRoomChatByChatId(
        @PathVariable Integer chatId
    ) {
        return ResponseEntity.ok(messageService.getAllMessagesOfRoomChatByChatId(chatId));
    }
}
