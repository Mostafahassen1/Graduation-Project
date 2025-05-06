
package com.codemeet.chat_peer_to_peer.controller_chat;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.codemeet.chat_peer_to_peer.entity_chat.ChatMessage;
import com.codemeet.chat_peer_to_peer.service_chat.ChatMessageService;
import com.codemeet.entity.User;

@RestController
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;

   private final ChatMessageService chatMessageService;

    public ChatMessageController(SimpMessagingTemplate simpleMessagingTemplate, ChatMessageService chatMessageService) {
        this.messagingTemplate = simpleMessagingTemplate;
        this.chatMessageService = chatMessageService;
    }

//    @MessageMapping("/chat")
//    public void processMessage(@Payload ChatMessage chatMessage) {
//
////        System.out.println("--------------------");
////        System.out.println(chatMessage.getMessage() );
////        System.out.println(chatMessage.getSender() );
////        System.out.println(chatMessage.getReceiver());
////        System.out.println("---------------------");
//
//        String receiverUsername = chatMessage.getReceiver().getUsername();
//
//        ChatMessage savedMsg = chatMessageService.save(chatMessage);
//        messagingTemplate.convertAndSendToUser(
//                receiverUsername, "/queue/messages",savedMsg );
//      //  System.out.println( "the message from /chat is: to save MSg " );
//    }

// Mostafa
   @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable User senderId,
                                                              @PathVariable User recipientId) {

           //    System.out.println("The message from message to find all chat before sending to the user");

        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));

    }


}


