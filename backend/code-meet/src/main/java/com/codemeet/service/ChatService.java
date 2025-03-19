package com.codemeet.service;

import com.codemeet.entity.Message;
import com.codemeet.entity.RoomChat;
import com.codemeet.repository.ChatRepository;
import com.codemeet.repository.MessageRepository;
import com.codemeet.utils.dto.chat.MessageInfoResponse;
import com.codemeet.utils.dto.chat.RoomChatInfoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserService userService;
    
    public ChatService(
        ChatRepository chatRepository,
        MessageRepository messageRepository,
        UserService userService
    ) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.userService = userService;
    }
    
    public List<RoomChat> getAllRoomChatEntitiesByUserId(Integer userId) {
        // Check the validity of userId...
        return chatRepository.findAllRoomChatsByUserId(userId);
    }
    
    public List<RoomChatInfoResponse> getAllRoomChatsByUserId(Integer userId) {
        return getAllRoomChatEntitiesByUserId(userId).stream()
            .map(RoomChatInfoResponse::of)
            .toList();
    }
    
    public List<Message> getAllMessageEntitiesByChatId(Integer chatId) {
        return messageRepository.findAllByChatId(chatId);
    }
    
    public List<MessageInfoResponse> getAllMessagesByChatId(Integer chatId) {
        return getAllMessageEntitiesByChatId(chatId).stream()
            .map(MessageInfoResponse::of)
            .toList();
    }
}
