package com.codemeet.service;

import com.codemeet.entity.*;
import com.codemeet.repository.ChatRepository;
import com.codemeet.utils.dto.chat.PeerChatInfoResponse;
import com.codemeet.utils.dto.chat.RoomChatInfoResponse;
import com.codemeet.utils.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ChatService {
    
    private final ChatRepository chatRepository;

    
    public Chat save(Chat chat) {
        return chatRepository.save(chat);
    }
    
    public List<Chat> saveAll(List<Chat> chats) {
        return chatRepository.saveAll(chats);
    }
    
    public Chat getChatEntityById(Integer chatId) {
        return chatRepository.findById(chatId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Chat with id '%d' not found".formatted(chatId)));
    }
    
    public PeerChat getPeerChatEntityById(Integer chatId) {
        if (getChatEntityById(chatId) instanceof PeerChat pc) {
            return pc;
        }
        throw new IllegalArgumentException(
            "There is no peer chat with id '%d'".formatted(chatId));
    }
    
    public RoomChat getRoomChatEntityById(Integer chatId) {
        if (getChatEntityById(chatId) instanceof RoomChat rc) {
            return rc;
        }
        throw new IllegalArgumentException(
            "There is no room chat with id '%d'".formatted(chatId));
    }
    
    public PeerChat getPeerChatEntityByOwnerIdAndPeerId(Integer ownerId, Integer peerId) {
        //TODO: Ensure that the owner and peer are friends...
        return chatRepository.findPeerChatEntityByOwnerIdAndPeerId(ownerId, peerId)
            .orElseThrow(() -> new EntityNotFoundException(
                "There is no chat between user with id '%d' and user with id '%d'"
                    .formatted(ownerId, peerId)));
    }
    
    public RoomChat getRoomChatEntityByOwnerIdAndRoomId(Integer ownerId, Integer roomId) {
        //TODO: Ensure that the owner is member of this room...
        return chatRepository.findRoomChatEntityByOwnerIdAndRoomId(ownerId, roomId)
            .orElseThrow(() -> new EntityNotFoundException(
                "User with id '%d' is not member in room with id '%d'"
                    .formatted(ownerId, roomId)));
    }
    
    public List<PeerChat> getAllPeerChatEntitiesByOwnerId(Integer ownerId) {
        return chatRepository.findAllPeerChatEntitiesByOwnerId(ownerId);
    }
    
    public List<RoomChat> getAllRoomChatEntitiesByOwnerId(Integer ownerId) {
        return chatRepository.findAllRoomChatEntitiesByOwnerId(ownerId);
    }
    
    public List<RoomChat> getAllRoomChatEntitiesByRoomId(Integer roomId) {
        return chatRepository.findAllRoomChatEntitiesByRoomId(roomId);
    }
    
    public PeerChatInfoResponse getPeerChatById(Integer chatId) {
        return PeerChatInfoResponse.of(getPeerChatEntityById(chatId));
    }
    
    public RoomChatInfoResponse getRoomChatById(Integer chatId) {
        return RoomChatInfoResponse.of(getRoomChatEntityById(chatId));
    }
    
    public PeerChatInfoResponse getPeerChatByOwnerIdAndPeerId(
        Integer ownerId, Integer peerId
    ) {
        return PeerChatInfoResponse.of(
            getPeerChatEntityByOwnerIdAndPeerId(ownerId, peerId));
    }
    
    public RoomChatInfoResponse getRoomChatByOwnerIdAndRoomId(
        Integer ownerId, Integer roomId
    ) {
        return RoomChatInfoResponse.of(
            getRoomChatEntityByOwnerIdAndRoomId(ownerId, roomId));
    }
    
    public List<PeerChatInfoResponse> getAllPeerChatsByOwnerId(Integer ownerId) {
        return getAllPeerChatEntitiesByOwnerId(ownerId).stream()
            .map(PeerChatInfoResponse::of)
            .toList();
    }
    
    public List<RoomChatInfoResponse> getAllRoomChatsByOwnerId(Integer ownerId) {
        return getAllRoomChatEntitiesByOwnerId(ownerId).stream()
            .map(RoomChatInfoResponse::of)
            .toList();
    }
}
