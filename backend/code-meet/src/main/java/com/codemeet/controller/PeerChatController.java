package com.codemeet.controller;

import com.codemeet.service.MessageService;
import com.codemeet.service.PeerChatService;
import com.codemeet.utils.dto.chat.MessageInfo;
import com.codemeet.utils.dto.chat.PeerChatInfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/peer-chat")
public class PeerChatController {
    
    private final PeerChatService peerChatService;
    private final MessageService messageService;
    
    public PeerChatController(
        PeerChatService peerChatService,
        MessageService messageService
    ) {
        this.peerChatService = peerChatService;
        this.messageService = messageService;
    }
    
    @GetMapping("{chatId}")
    public ResponseEntity<PeerChatInfoResponse> getPeerChatById(
        @PathVariable Integer chatId
    ) {
        return ResponseEntity.ok(peerChatService.getPeerChatById(chatId));
    }
    
    @GetMapping("/{oneId}/{theOtherId}")
    public ResponseEntity<PeerChatInfoResponse> getPeerChatByOneAndTheOtherIds(
        @PathVariable Integer oneId,
        @PathVariable Integer theOtherId
    ) {
        return ResponseEntity.ok(
            peerChatService.getPeerChatByOneAndTheOtherIds(oneId, theOtherId)
        );
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PeerChatInfoResponse>> getAllPeerChatsByUserId(
        @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(peerChatService.getAllPeerChatsByUserId(userId));
    }
    
    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<MessageInfo>> getAllMessagesOfChat(
        @PathVariable Integer chatId
    ) {
        return ResponseEntity.ok(messageService.getAllMessagesOfChat(chatId));
    }
}
