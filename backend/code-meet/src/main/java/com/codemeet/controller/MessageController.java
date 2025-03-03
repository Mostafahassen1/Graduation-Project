package com.codemeet.controller;

import com.codemeet.service.MessageService;
import com.codemeet.utils.dto.P2PMessageInfoResponse;
import com.codemeet.utils.dto.RoomMessageInfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<RoomMessageInfoResponse>> getAllMessagesOfRoom(
        @PathVariable Integer roomId
    ) {
        return ResponseEntity.ok(messageService.getAllMessagesOfRoom(roomId));
    }

    @GetMapping("/p1/{p1Id}/p2/{p2Id}")
    public ResponseEntity<List<P2PMessageInfoResponse>> getAllMessagesWithPeer(
        @PathVariable int p1Id, @PathVariable int p2Id
    ) {
        return ResponseEntity.ok(messageService.getAllMessagesWithPeer(p1Id, p2Id));
    }
}
