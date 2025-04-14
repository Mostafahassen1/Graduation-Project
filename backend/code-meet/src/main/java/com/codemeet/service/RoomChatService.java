package com.codemeet.service;

import com.codemeet.entity.RoomChat;
import com.codemeet.repository.RoomChatRepository;
import com.codemeet.utils.dto.chat.RoomChatInfoResponse;
import com.codemeet.utils.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomChatService {
    
    private final RoomChatRepository roomChatRepository;
    
    public RoomChatService(RoomChatRepository roomChatRepository) {
        this.roomChatRepository = roomChatRepository;
    }
    
    public RoomChat getRoomChatEntityById(Integer chatId) {
        return roomChatRepository.findById(chatId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Chat with id '%d' not found".formatted(chatId)));
    }
    
    public RoomChat getRoomChatEntityByRoomId(Integer roomId) {
        return roomChatRepository.findByRoomId(roomId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Chat with roomId '%d' not found".formatted(roomId)));
    }
    
    public List<RoomChat> getAllRoomChatEntitiesByUserId(Integer userId) {
        return roomChatRepository.findAllByUserId(userId);
    }
    
    public boolean isMemberOfChat(Integer userId, Integer chatId) {
        return roomChatRepository.isMember(userId, chatId);
    }
    
    public RoomChatInfoResponse getRoomChatById(Integer chatId) {
        return RoomChatInfoResponse.of(getRoomChatEntityById(chatId));
    }
    
    public RoomChatInfoResponse getRoomChatByRoomId(Integer roomId) {
        return RoomChatInfoResponse.of(getRoomChatEntityByRoomId(roomId));
    }
    
    public List<RoomChatInfoResponse> getAllRoomChatsOfUser(Integer userId) {
        return getAllRoomChatEntitiesByUserId(userId).stream()
            .map(RoomChatInfoResponse::of)
            .toList();
    }
}
