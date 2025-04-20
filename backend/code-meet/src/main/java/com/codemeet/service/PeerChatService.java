package com.codemeet.service;

import com.codemeet.entity.PeerChat;
import com.codemeet.repository.PeerChatRepository;
import com.codemeet.utils.dto.chat.PeerChatInfoResponse;
import com.codemeet.utils.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeerChatService {
    
    private final PeerChatRepository peerChatRepository;
    
    public PeerChatService(PeerChatRepository peerChatRepository) {
        this.peerChatRepository = peerChatRepository;
    }
    
    public PeerChat getPeerChatEntityById(Integer chatId) {
        return peerChatRepository.findById(chatId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Chat with id '%d' not found".formatted(chatId)));
    }
    
    public PeerChat getPeerChatEntityByOneAndTheOtherIds(Integer oneId, Integer theOtherId) {
        return peerChatRepository.findByOneAndTheOtherIds(oneId, theOtherId)
            .orElseThrow(() -> new EntityNotFoundException(
                "There is no chat between user with id '%d' and user with id '%d'"
                    .formatted(oneId, theOtherId)));
    }
    
    public List<PeerChat> getAllPeerChatEntitiesByUserId(Integer userId) {
        return peerChatRepository.findAllByUserId(userId);
    }
    
    public PeerChatInfoResponse getPeerChatById(Integer chatId) {
        return PeerChatInfoResponse.of(getPeerChatEntityById(chatId));
    }
    
    public PeerChatInfoResponse getPeerChatByOneAndTheOtherIds(Integer oneId, Integer theOtherId) {
        return PeerChatInfoResponse.of(
            getPeerChatEntityByOneAndTheOtherIds(oneId, theOtherId));
    }
    
    public List<PeerChatInfoResponse> getAllPeerChatsByUserId(Integer userId) {
        return getAllPeerChatEntitiesByUserId(userId).stream()
            .map(PeerChatInfoResponse::of)
            .toList();
    }
}
