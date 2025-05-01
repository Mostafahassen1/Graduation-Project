package com.codemeet.controller;

import com.codemeet.service.MessageService;
import com.codemeet.utils.dto.chat.MessageInfoRequest;
import com.codemeet.utils.dto.chat.MessageInfoResponse;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
@AllArgsConstructor
@Controller
public class WebSocketController {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    

    
//    @MessageMapping("/peer-chat")
//    public void sendToPeer(@Payload MessageInfoRequest messageInfoRequest) {
//        MessageInfoResponse messageInfoResponse =
//            messageService.save(messageInfoRequest);
//
//        String destination = "/chat/" + messageInfoRequest.chatId();
//        messagingTemplate.convertAndSend(destination, messageInfo);
//    }
//
//    @MessageMapping("/room-chat")
//    public void sendToRoom(@Payload MessageInfoRequest messageInfo) {
//        messageInfo = messageService.save(messageInfo);
//
//        String destination = "/chat/" + messageInfo.chatId();
//        messagingTemplate.convertAndSend(destination, messageInfo);
//    }
}
