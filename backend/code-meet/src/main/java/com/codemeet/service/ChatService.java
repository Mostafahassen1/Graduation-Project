package com.codemeet.service;

import com.codemeet.entity.*;
import com.codemeet.repository.ChatRepository;
import com.codemeet.utils.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    
    private final ChatRepository chatRepository;
    
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }
    
    public Chat save(Chat chat) {
        return chatRepository.save(chat);
    }
    
    public Chat getChatEntityById(Integer chatId) {
        return chatRepository.findById(chatId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Chat with id '%d' not found".formatted(chatId)));
    }
}
